package school.hei.patrimoine.modele.comptable.fec.io;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.MouvementComptable.DEBIT;
import static school.hei.patrimoine.modele.comptable.TypeComptable.BANQUE;
import static school.hei.patrimoine.modele.comptable.fec.JournalCode.JN;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import school.hei.patrimoine.modele.comptable.CompteComptable;
import school.hei.patrimoine.modele.comptable.fec.EcritureComptable;
import school.hei.patrimoine.modele.comptable.fec.Journal;
import school.hei.patrimoine.modele.comptable.fec.LigneEcriture;
import school.hei.patrimoine.modele.possession.Compte;

class FECWriterTest {

  @TempDir Path tempDir;

  private Journal journalAvecEcritures(int n) {
    var ecritures = new ArrayList<EcritureComptable>();
    for (int i = 0; i < n; i++) {
      ecritures.add(
          EcritureComptable.builder()
              .id("AC" + String.format("%03d", i))
              .date(LocalDate.of(2024, JANUARY, 1))
              .libelle("Libellé " + i)
              .valeur(ariary(100_000))
              .pj(null)
              .dateValidation(LocalDate.of(2024, JANUARY, 2))
              .lignes(List.of(makeLigne()))
              .build());
    }
    return new Journal(JN, "Achats", ecritures);
  }

  private LigneEcriture makeLigne() {
    var compte = new Compte("Banque test", LocalDate.of(2024, JANUARY, 1), ariary(100));
    var compteComptable =
        CompteComptable.builder()
            .compte(compte)
            .typeComptable(BANQUE)
            .mouvementComptable(DEBIT)
            .build();
    return LigneEcriture.builder()
        .compteComptable(compteComptable)
        .compteAuxiliaire(null)
        .lettrage(null)
        .dateLettrage(null)
        .build();
  }

  private byte[] bytesOf(Path p) throws IOException {
    return Files.readAllBytes(p);
  }

  private String textOf(Path p) throws IOException {
    return new String(bytesOf(p), StandardCharsets.UTF_8);
  }

  private long lignesNonVides(Path p) throws IOException {
    return textOf(p).lines().filter(l -> !l.isBlank()).count();
  }

  private String sansBOM(Path p) throws IOException {
    var stringPath = textOf(p);
    return stringPath.startsWith("\uFEFF") ? stringPath.substring(1) : stringPath;
  }

  @Test
  void le_fichier_commence_par_le_BOM_UTF8() throws IOException {
    var file = tempDir.resolve("bom.csv");
    try (var writer = new FECWriter(file)) {
      writer.writeFEC(Collections.emptyList());
    }

    var bytes = bytesOf(file);
    assertTrue(bytes.length >= 3, "Le fichier ne doit pas être vide");
    assertEquals((byte) 0xEF, bytes[0], "Octet BOM[0] attendu : 0xEF");
    assertEquals((byte) 0xBB, bytes[1], "Octet BOM[1] attendu : 0xBB");
    assertEquals((byte) 0xBF, bytes[2], "Octet BOM[2] attendu : 0xBF");
  }

  @Test
  void collection_vide_produit_uniquement_le_header() throws IOException {
    var file = tempDir.resolve("vide.csv");
    try (var writer = new FECWriter(file)) {
      writer.writeFEC(Collections.emptyList());
    }

    assertEquals(
        1,
        lignesNonVides(file),
        "Collection vide : seul le header doit être présent (1 ligne non vide)");
  }

  @Test
  void le_header_utilise_le_separateur_tabulation() throws IOException {
    var file = tempDir.resolve("tsv.csv");
    try (var writer = new FECWriter(file)) {
      writer.writeFEC(Collections.emptyList());
    }

    var premiereLigne = sansBOM(file).lines().findFirst().orElse("");
    assertTrue(
        premiereLigne.contains("\t"),
        "Le header doit contenir des tabulations (format TSV). Obtenu : " + premiereLigne);
  }

  @Test
  void le_header_ne_contient_pas_de_guillemets() throws IOException {
    var file = tempDir.resolve("noQuote.csv");
    try (var writer = new FECWriter(file)) {
      writer.writeFEC(Collections.emptyList());
    }

    var premiereLigne = sansBOM(file).lines().findFirst().orElse("");
    assertFalse(
        premiereLigne.contains("\""),
        "Le header ne doit pas contenir de guillemets (NO_QUOTE_CHARACTER)");
  }

  @Test
  void nombre_de_lignes_correspond_aux_lignes_ecriture() throws IOException {
    var n = 3;
    var file = tempDir.resolve("data.csv");
    try (var writer = new FECWriter(file)) {
      writer.writeFEC(List.of(journalAvecEcritures(n)));
    }

    assertEquals(1 + n, lignesNonVides(file), "Attendu : 1 header + " + n + " lignes de données");
  }

  @Test
  void chunking_exactement_1000_lignes_aucune_perte() throws IOException {
    var n = 1_000;
    var file = tempDir.resolve("chunk1000.csv");
    try (var writer = new FECWriter(file)) {
      writer.writeFEC(List.of(journalAvecEcritures(n)));
    }

    assertEquals(
        1 + n,
        lignesNonVides(file),
        "À la limite exacte du chunk (1000) toutes les lignes écrites");
  }

  @Test
  void chunking_1001_lignes_aucune_perte() throws IOException {
    var n = 1_001;
    var file = tempDir.resolve("chunk1001.csv");
    try (var writer = new FECWriter(file)) {
      writer.writeFEC(List.of(journalAvecEcritures(n)));
    }

    assertEquals(
        1 + n,
        lignesNonVides(file),
        "Au-delà de 1000 lignes, le 2ème chunk doit aussi être écrit sans perte");
  }

  @Test
  void plusieurs_journaux_accumulent_toutes_les_lignes() throws IOException {
    var file = tempDir.resolve("multi.csv");
    try (var writer = new FECWriter(file)) {
      writer.writeFEC(List.of(journalAvecEcritures(2), journalAvecEcritures(3)));
    }

    assertEquals(
        1 + 2 + 3, lignesNonVides(file), "2 journaux (2+3 écritures) → 5 lignes de données");
  }

  @Test
  void fichier_est_en_utf8_valide() throws IOException {
    var file = tempDir.resolve("utf8.csv");
    try (var writer = new FECWriter(file)) {
      writer.writeFEC(Collections.emptyList());
    }

    assertDoesNotThrow(
        () -> {
          String contenu = new String(bytesOf(file), StandardCharsets.UTF_8);
          assertFalse(contenu.isEmpty(), "Le contenu ne doit pas être vide");
        });
  }

  @Test
  void fichier_lisible_apres_close_explicite() throws IOException {
    var file = tempDir.resolve("close.csv");
    var writer = new FECWriter(file);
    writer.writeFEC(Collections.emptyList());
    writer.close();

    assertTrue(Files.exists(file), "Le fichier doit exister après close()");
    assertTrue(Files.size(file) > 0, "Le fichier ne doit pas être vide après close()");
  }

  @Test
  void try_with_resources_ne_leve_pas_dexception() {
    var file = tempDir.resolve("twr.csv");
    assertDoesNotThrow(
        () -> {
          try (var w = new FECWriter(file)) {
            w.writeFEC(Collections.emptyList());
          }
        },
        "try-with-resources sur FECWriter ne doit pas lever d'exception");
  }

  @Test
  void writeFEC_appele_deux_fois_accumule_les_lignes() throws IOException {
    var file = tempDir.resolve("double.csv");
    try (var writer = new FECWriter(file)) {
      writer.writeFEC(List.of(journalAvecEcritures(2)));
      writer.writeFEC(List.of(journalAvecEcritures(3)));
    }

    long lignes = lignesNonVides(file);
    assertTrue(
        lignes >= 2 + 3,
        "Deux appels à writeFEC doivent produire au moins les lignes de données des deux appels");
  }
}
