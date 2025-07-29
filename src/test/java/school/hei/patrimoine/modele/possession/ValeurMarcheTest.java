package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.ValeurMarche.ValeurMarche;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValeurMarcheTest {

    @Test
    void valeurMarche_peut_etre_attribuee_a_un_materiel() {
        // Given
        Materiel materiel = new Materiel(
                "Ordinateur",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 1),
                new Argent(1_000, Devise.MGA),
                0.05
        );
        LocalDate dateValeur = LocalDate.of(2023, 6, 1);
        Argent valeur = new Argent(1_200, Devise.MGA);

        // When
        assertDoesNotThrow(() -> new ValeurMarche(materiel, dateValeur, valeur));

        // Then
        assertEquals(valeur, materiel.valeurMarcheALaDate(dateValeur));
    }

    @Test
    void valeurMarche_ne_peut_pas_etre_attribuee_a_un_compte() {
        // Given
        Compte compte = new Compte(
                "Compte courant",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 1),
                new Argent(2_000, Devise.MGA)
        );
        LocalDate dateValeur = LocalDate.of(2023, 6, 1);
        Argent valeur = new Argent(2_500, Devise.MGA);

        // When
        Executable executable = () -> new ValeurMarche(compte, dateValeur, valeur);

        // Then
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, executable);
        assertEquals("Seules les possessions de type IMMOBILISATION ou ENTREPRISE peuvent avoir une valeur de marché.", exception.getMessage());
    }
}
