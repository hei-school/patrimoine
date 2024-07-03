package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyTest {
    @Test
    void projection_patrimoine_zety_le_17_septembre_2024() {
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

        var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
        var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
        var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);

        var fraisDeScolarite = new FluxArgent(
                "Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27),
                LocalDate.of(2024, AUGUST, 27), -200_000, 27);

        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte", compteBancaire, au3juillet24.withDayOfMonth(25),
                LocalDate.of(2024, DECEMBER, 25), -20_000, 25);

        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineZetyAu3juillet24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, argentEspeces, fraisDeScolarite, compteBancaire, fraisTenueCompte));

        assertEquals(2978848, patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable());
    }
}
