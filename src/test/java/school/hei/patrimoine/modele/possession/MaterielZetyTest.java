package school.hei.patrimoine.modele.possession;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaterielZetyTest {
    @Test
    void la_valeur_de_l_ordinateur_de_zety_le_17_septembre_2024() {
        var au3juillet2024 = LocalDate.of(2024, JULY, 3);
        var ordinateur_de_zety = new Materiel(
                "Ordinateur de Zety",
                au3juillet2024,
                1_200_000,
                au3juillet2024,
                -0.1);

        var au17sept2024 = LocalDate.of(2024, SEPTEMBER, 17);
        assertEquals(1_174_684, ordinateur_de_zety.valeurComptableFuture(au17sept2024));
    }

    @Test
    void la_valeur_des_habits_de_zety_le_17_septembre_2024() {
        var au3juillet2024 = LocalDate.of(2024, JULY, 3);
        var habits_de_zety = new Materiel(
                "Les Habits de Zety",
                au3juillet2024,
                1_500_000,
                au3juillet2024,
                -0.5);

        var au17sept2024 = LocalDate.of(2024, SEPTEMBER, 17);
        assertEquals(1_341_780, habits_de_zety.valeurComptableFuture(au17sept2024));
    }
}
