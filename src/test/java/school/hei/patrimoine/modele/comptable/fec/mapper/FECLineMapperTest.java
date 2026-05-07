package school.hei.patrimoine.modele.comptable.fec.mapper;

import static java.time.LocalDate.now;
import static java.time.Month.*;
import static java.util.Locale.US;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.EUR;
import static school.hei.patrimoine.modele.comptable.fec.JournalCode.JN;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.comptable.fec.factory.JournalFactory;
import school.hei.patrimoine.modele.possession.*;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

class FECLineMapperTest {
  private FECLineMapper mapper;
  private PieceJustificative pj;
  private Compte compteDebiteur;
  private Compte compteCrediteur;

  @BeforeEach
  void setup() {
    mapper = new FECLineMapper(new CompteNumResolver());
    pj = new PieceJustificative("Transfert Argent BNI", LocalDate.of(2026, 4, 5), "lien-facture");
    compteDebiteur = new Compte("Compte débiteur", LocalDate.of(2026, 1, 1), ariary(100_000));
    compteCrediteur = new Compte("Compte créditeur", LocalDate.of(2025, 1, 31), ariary(5_000_000));
  }

  @Test
  void values_should_match() {
    var transfert =
        new TransfertArgent(
            "Transfert Argent BNI",
            compteCrediteur,
            compteDebiteur,
            LocalDate.of(2026, 5, 3),
            ariary(1_000_000));
    var optionalOperation = OperationComptable.of(transfert);
    var operation = optionalOperation.orElseThrow();
    var journal =
        JournalFactory.make(JN, "Journal", Set.of(operation), Map.of("Transfert Argent BNI", pj));
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();
    var montantEUR = formatAmount(ecriture.valeur().convertir(EUR, now()).montant());
    var montantMGA = formatAmount(ecriture.valeur().montant());

    var fecLine = mapper.toFECLine(journal, ecriture, ligne);
    var values = fecLine.values();

    assertEquals("JN", values[0], "JournalCode");
    assertEquals("Journal", values[1], "JournalLib");
    assertEquals("JN000", values[2], "EcritureNum");
    assertEquals("20260503", values[3], "EcritureDate");
    assertEquals("658001", values[4], "CompteNum");
    assertEquals("Compte débiteur", values[5], "CompteLib");
    assertEquals("", values[6], "CompAuxNum");
    assertEquals("", values[7], "CompAuxLib");
    assertEquals("Transfert Argent BNI", values[8], "PieceRef");
    assertEquals("20260405", values[9], "PieceDate");
    assertEquals("Transfert Argent BNI", values[10], "EcritureLib");
    assertEquals(montantEUR, values[11], "Debit");
    assertEquals("0.00", values[12], "Credit");
    assertEquals("", values[13], "EcritureLet");
    assertEquals("", values[14], "DateLet");
    assertEquals("20260405", values[15], "ValidDate");
    assertEquals(montantMGA, values[16], "Montantdevise");
    assertEquals("MGA", values[17], "Idevise");
  }

  @Test
  void debit_and_credit_must_be_balanced() {
    var achat =
        new AchatMaterielAuComptant(
            "Voiture", LocalDate.of(2025, 6, 18), ariary(500_000_000), 1.5, compteCrediteur);
    var optionalOperation = OperationComptable.of(achat);
    var operation = optionalOperation.orElseThrow();
    var journal =
        JournalFactory.make(JN, "Journal", Set.of(operation), Map.of("Achat d'une voiture", pj));
    var ecriture = journal.ecritures().getFirst();
    var ligne1 = ecriture.lignes().get(0);
    var ligne2 = ecriture.lignes().get(1);

    var values1 = mapper.toFECLine(journal, ecriture, ligne1).values();
    var values2 = mapper.toFECLine(journal, ecriture, ligne2).values();

    assertEquals(values1[11], values2[12]);
    assertEquals(values1[12], values2[11]);
  }

  private static String formatAmount(double montant) {
    return String.format(US, "%.2f", Math.abs(montant));
  }

  @Test
  void transfert_compte_num_est_virement_interne_580() {
    var transfert =
        new TransfertArgent(
            "Transfert Argent BNI",
            compteCrediteur,
            compteDebiteur,
            LocalDate.of(2026, 5, 3),
            ariary(1_000_000));
    var optionalOperation = OperationComptable.of(transfert);
    var operation = optionalOperation.orElseThrow();
    var journal =
        JournalFactory.make(JN, "Journal", Set.of(operation), Map.of("Transfert Argent BNI", pj));
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = mapper.toFECLine(journal, ecriture, ligne);

    assertEquals("658001", fecLine.values()[4]);
  }

  @Test
  void transfert_debiteur_est_vers_compte() {
    var transfert =
        new TransfertArgent(
            "Transfert Argent BNI",
            compteCrediteur,
            compteDebiteur,
            LocalDate.of(2026, 5, 3),
            ariary(1_000_000));
    var optionalOperation = OperationComptable.of(transfert);
    var operation = optionalOperation.orElseThrow();
    var journal =
        JournalFactory.make(JN, "Journal", Set.of(operation), Map.of("Transfert Argent BNI", pj));
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = mapper.toFECLine(journal, ecriture, ligne);

    assertEquals("Compte débiteur", fecLine.values()[5]);
    assertFalse(fecLine.values()[11].isEmpty());
    assertEquals("0.00", fecLine.values()[12]);
  }

  @Test
  void transfert_crediteur_est_depuis_compte() {
    var transfert =
        new TransfertArgent(
            "Transfert Argent BNI",
            compteCrediteur,
            compteDebiteur,
            LocalDate.of(2026, 5, 3),
            ariary(1_000_000));
    var optionalOperation = OperationComptable.of(transfert);
    var operation = optionalOperation.orElseThrow();
    var journal =
        JournalFactory.make(JN, "Journal", Set.of(operation), Map.of("Transfert Argent BNI", pj));
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().get(1);

    var fecLine = mapper.toFECLine(journal, ecriture, ligne);

    assertEquals("512001", fecLine.values()[4]);
    assertEquals("Compte créditeur", fecLine.values()[5]);
    assertEquals("0.00", fecLine.values()[11]);
    assertFalse(fecLine.values()[12].isEmpty());
  }

  @Test
  void transfert_champs_pj_sont_correctement_remplis() {
    var transfert =
        new TransfertArgent(
            "Transfert Argent BNI",
            compteCrediteur,
            compteDebiteur,
            LocalDate.of(2026, 5, 3),
            ariary(1_000_000));
    var optionalOperation = OperationComptable.of(transfert);
    var operation = optionalOperation.orElseThrow();
    var journal =
        JournalFactory.make(JN, "Journal", Set.of(operation), Map.of("Transfert Argent BNI", pj));
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = mapper.toFECLine(journal, ecriture, ligne);

    assertEquals("Transfert Argent BNI", fecLine.values()[8]);
    assertEquals("20260405", fecLine.values()[9]);
    assertEquals("20260405", fecLine.values()[15]);
  }

  @Test
  void flux_positif_compte_num_est_banque_512() {
    var flux =
        new FluxArgent("Vente", compteCrediteur, LocalDate.of(2026, JANUARY, 10), ariary(200_000));
    var optionalOperation = OperationComptable.of(flux);
    var operation = optionalOperation.orElseThrow();
    var journal = JournalFactory.make(JN, "Journal", Set.of(operation), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = mapper.toFECLine(journal, ecriture, ligne);

    assertEquals("512001", fecLine.values()[4]);
    assertEquals("0.00", fecLine.values()[12]);
    assertFalse(fecLine.values()[11].isEmpty());
  }

  @Test
  void flux_positif_passif_compte_num_est_pca_487() {
    var flux =
        new FluxArgent("Vente", compteCrediteur, LocalDate.of(2026, JANUARY, 10), ariary(200_000));
    var optionalOperation = OperationComptable.of(flux);
    var operation = optionalOperation.orElseThrow();
    var journal = JournalFactory.make(JN, "Journal", Set.of(operation), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().get(1);

    var fecLine = mapper.toFECLine(journal, ecriture, ligne);

    assertEquals("487001", fecLine.values()[4]);
    assertEquals("0.00", fecLine.values()[11]);
    assertFalse(fecLine.values()[12].isEmpty());
  }

  @Test
  void flux_negatif_compte_num_est_cca_486() {
    var flux =
        new FluxArgent("Loyer", compteCrediteur, LocalDate.of(2026, JANUARY, 10), ariary(-200_000));
    var optionalOperation = OperationComptable.of(flux);
    var operation = optionalOperation.orElseThrow();
    var journal = JournalFactory.make(JN, "Journal", Set.of(operation), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = mapper.toFECLine(journal, ecriture, ligne);

    assertEquals("486001", fecLine.values()[4]);
    assertFalse(fecLine.values()[11].isEmpty());
    assertEquals("0.00", fecLine.values()[12]);
  }

  @Test
  void flux_negatif_passif_compte_num_est_banque_512() {
    var flux =
        new FluxArgent("Loyer", compteCrediteur, LocalDate.of(2026, JANUARY, 10), ariary(-200_000));
    var optionalOperation = OperationComptable.of(flux);
    var operation = optionalOperation.orElseThrow();
    var journal = JournalFactory.make(JN, "Journal", Set.of(operation), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().get(1);

    var fecLine = mapper.toFECLine(journal, ecriture, ligne);

    assertEquals("512001", fecLine.values()[4]);
  }

  @Test
  void achat_materiel_compte_num_est_materiel_2183() {
    var achat =
        new AchatMaterielAuComptant(
            "Ordinateur", LocalDate.of(2026, JANUARY, 10), ariary(500_000), 2.4, compteCrediteur);
    var optionalOperation = OperationComptable.of(achat);
    var operation = optionalOperation.orElseThrow();
    var journal = JournalFactory.make(JN, "Journal", Set.of(operation), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = mapper.toFECLine(journal, ecriture, ligne);

    assertEquals("2183001", fecLine.values()[4]);
    assertFalse(fecLine.values()[11].isEmpty());
  }

  @Test
  void achat_materiel_passif_compte_num_est_banque_512() {
    var achat =
        new AchatMaterielAuComptant(
            "Ordinateur", LocalDate.of(2026, JANUARY, 10), ariary(500_000), 2.4, compteCrediteur);
    var optionalOperation = OperationComptable.of(achat);
    var operation = optionalOperation.orElseThrow();
    var journal = JournalFactory.make(JN, "Journal", Set.of(operation), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().get(1);

    var fecLine = mapper.toFECLine(journal, ecriture, ligne);

    assertEquals("512001", fecLine.values()[4]);
  }

  @Test
  void remboursement_dette_compte_num_est_164() {
    var dette = new Dette("Dette", LocalDate.of(2024, JULY, 1), ariary(-500_000));
    var creance = new Creance("Créance", LocalDate.of(2024, JULY, 1), ariary(500_000));
    var remboursement =
        new RemboursementDette(
            "Rembourserment",
            compteCrediteur,
            compteDebiteur,
            dette,
            creance,
            LocalDate.of(2026, JANUARY, 31),
            ariary(500_000));
    var optionalOperation = OperationComptable.of(remboursement);
    var operation = optionalOperation.orElseThrow();
    var journal = JournalFactory.make(JN, "Journal", Set.of(operation), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = mapper.toFECLine(journal, ecriture, ligne);

    assertEquals("164001", fecLine.values()[4]);
  }

  @Test
  void meme_compte_recoit_toujours_le_meme_numero_dans_un_export() {
    var flux1 =
        new FluxArgent(
            "Vente A", compteCrediteur, LocalDate.of(2026, JANUARY, 10), ariary(200_000));
    var flux2 =
        new FluxArgent(
            "Vente B", compteCrediteur, LocalDate.of(2026, FEBRUARY, 10), ariary(300_000));
    var optionalOperation1 = OperationComptable.of(flux1);
    var operation1 = optionalOperation1.orElseThrow();
    var optionalOperation2 = OperationComptable.of(flux2);
    var operation2 = optionalOperation2.orElseThrow();
    var journal = JournalFactory.make(JN, "Journal", Set.of(operation1, operation2), Map.of());

    var ecriture1 = journal.ecritures().getFirst();
    var ecriture2 = journal.ecritures().get(1);

    var fecLine1 = mapper.toFECLine(journal, ecriture1, ecriture1.lignes().getFirst());
    var fecLine2 = mapper.toFECLine(journal, ecriture2, ecriture2.lignes().getFirst());

    assertEquals(fecLine1.values()[4], fecLine2.values()[4]);
  }
}
