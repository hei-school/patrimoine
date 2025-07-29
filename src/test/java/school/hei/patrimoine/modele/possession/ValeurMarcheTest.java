package school.hei.patrimoine.modele.possession;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.ValeurMarche.ValeurMarche;

class ValeurMarcheTest {

  @Test
  void valeurMarche_peut_etre_attribuee_a_un_materiel() {
    assertDoesNotThrow(
        () ->
            new ValeurMarche(
                new Materiel(
                    "Ordinateur",
                    LocalDate.of(2023, 1, 1),
                    LocalDate.of(2023, 1, 1),
                    new Argent(1_000, Devise.MGA),
                    0.05),
                LocalDate.of(2023, 6, 1),
                new Argent(900, Devise.MGA)));
  }

  @Test
  void valeurMarche_ne_peut_pas_etre_attribuee_a_un_compte() {
    Executable executable =
        () ->
            new ValeurMarche(
                new Compte(
                    "Compte courant",
                    LocalDate.of(2023, 1, 1),
                    LocalDate.of(2023, 1, 1),
                    new Argent(2_000, Devise.MGA)),
                LocalDate.of(2023, 6, 1),
                new Argent(2_500, Devise.MGA));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
    assertEquals(
        "Seules les possessions de type IMMOBILISATION ou ENTREPRISE peuvent avoir une valeur de"
            + " marché.",
        exception.getMessage());
  }
}
