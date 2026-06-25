package school.hei.patrimoine.patrilang.files.validator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import school.hei.patrimoine.patrilang.files.PatriLangFile;

class SupportingInfoFileValidatorTest {
  private final SupportingInfoFileValidator subject = new SupportingInfoFileValidator();

  @TempDir Path tempDir;

  private PatriLangFile writePjFile(String content) throws IOException {
    var file = tempDir.resolve("test.info.md").toFile();
    Files.writeString(file.toPath(), content);
    return new PatriLangFile(file);
  }

  @Test
  void accepte_fichier_pj_valide_avec_une_seule_pj() throws IOException {
    var pjFile =
        writePjFile(
            """
            # Général
            * Spécifier le 25 janvier 2026
            * Cas de Exemple

            # Pièces Justificatives
            * `salaire`, le 1 janvier 2026, REF-SALAIRE-001, "https://example.com/salaire.pdf"
            """);

    assertDoesNotThrow(() -> subject.accept(pjFile));
  }

  @Test
  void accepte_fichier_pj_valide_avec_plusieurs_pj_ids_distincts() throws IOException {
    var pjFile =
        writePjFile(
            """
            # Général
            * Spécifier le 25 janvier 2025
            * Cas de Exemple

            # Pièces Justificatives
            * `salaire`, le 1 janvier 2026, REF-SALAIRE-001, "https://example.com/salaire.pdf"
            * `loyer`, le 2 janvier 2026, REF-LOYER-001, "https://example.com/loyer.pdf"
            * `transport`, le 3 janvier 2026, REF-TRANS-001, "https://example.com/transport.pdf"
            """);

    assertDoesNotThrow(() -> subject.accept(pjFile));
  }

  @Test
  void accepte_fichier_pj_vide_sans_section_pj() throws IOException {
    var pjFile =
        writePjFile(
            """
            # Général
            * Spécifier le 26 janvier 2026
            * Cas de Exemple

            # Pièces Justificatives
            """);

    assertDoesNotThrow(() -> subject.accept(pjFile));
  }

  @Test
  void rejette_deux_pj_avec_le_meme_id() throws IOException {
    var pjFile =
        writePjFile(
            """
            # Général
            * Spécifier le 26 janvier 2026
            * Cas de Exemple

            # Pièces Justificatives
            * `salaire`, le 1 janvier 2026, REF-SALAIRE-001, "https://example.com/salaire1.pdf"
            * `salaire`, le 2 janvier 2026, REF-SALAIRE-002, "https://example.com/salaire2.pdf"
            """);

    var ex = assertThrows(IllegalArgumentException.class, () -> subject.accept(pjFile));
    assertTrue(
        ex.getMessage().contains("salaire"), "Le message d'erreur doit contenir l'id dupliqué");
    assertTrue(
        ex.getMessage().contains("existe déjà"),
        "Le message d'erreur doit préciser que la PJ existe déjà");
  }

  @Test
  void message_erreur_contient_id_exact_du_doublon() throws IOException {
    var pjFile =
        writePjFile(
            """
            # Général
            * Spécifier le 26 janvier 2026
            * Cas de Exemple

            # Pièces Justificatives
            * `assuranceVoiture`, le 1 janvier 2026, REF-ASSURANCE-001, "https://example.com/a.pdf"
            * `loyer`, le 2 janvier 2026, REF-LOYER-001, "https://example.com/b.pdf"
            * `assuranceVoiture`, le 3 janvier 2026, REF-ASSURANCE-001, "https://example.com/c.pdf"
            """);

    var ex = assertThrows(IllegalArgumentException.class, () -> subject.accept(pjFile));
    assertTrue(ex.getMessage().contains("assuranceVoiture"));
    assertTrue(ex.getMessage().contains("Une opération ne devrait avoir qu'une seule pj"));
  }

  @Test
  void rejette_meme_si_le_doublon_est_le_troisieme_element() throws IOException {
    var pjFile =
        writePjFile(
            """
            # Général
            * Spécifier le 26 janvier 2026
            * Cas de Exemple

            # Pièces Justificatives
            * `pj1`, le 1 janvier 2026, REF-PJ-001, "https://example.com/1.pdf"
            * `pj2`, le 2 janvier 2026,REF-PJ-002, "https://example.com/2.pdf"
            * `pj3`, le 3 janvier 2026,REF-PJ-003, "https://example.com/3.pdf"
            * `pj1`, le 4 janvier 2026,REF-PJ-001, "https://example.com/1bis.pdf"
            """);

    var ex = assertThrows(IllegalArgumentException.class, () -> subject.accept(pjFile));
    assertTrue(ex.getMessage().contains("pj1"));
  }
}
