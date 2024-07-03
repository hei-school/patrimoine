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
        var au03juillet24 = LocalDate.of(2024, JULY, 3);
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

        var ordinateur = new Materiel("Ordinateur", au03juillet24, 1_200_000, au03juillet24, -0.10);
        var vetements = new Materiel("Vêtements", au03juillet24, 1_500_000, au03juillet24, -0.50);
        var argentEspeces = new Argent("Espèces", au03juillet24, 800_000);

        var fraisDeScolarite = new FluxArgent(
                "Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27),
                LocalDate.of(2024, AUGUST, 27), -200_000, 27);

        var compteBancaire = new Argent("Compte bancaire", au03juillet24, 100_000);
        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte", compteBancaire, au03juillet24.withDayOfMonth(25),
                LocalDate.of(2024, DECEMBER, 25), -20_000, 25);

        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineZetyAu3juillet24",
                zety,
                au03juillet24,
                Set.of(ordinateur, vetements, argentEspeces, fraisDeScolarite, compteBancaire, fraisTenueCompte));

        assertEquals(2978848, patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable());
    }

    @Test
    void diminution_patrimoine_zety_entre_17_et_18_septembre_2024() {
        var zety = new Personne("Zety");
        var au03juillet24 = LocalDate.of(2024, JULY, 3);
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

        var ordinateur = new Materiel("Ordinateur", au03juillet24, 1_200_000, au03juillet24, -0.10);
        var vetements = new Materiel("Vêtements", au03juillet24, 1_500_000, au03juillet24, -0.50);
        var argentEspeces = new Argent("Espèces", au03juillet24, 800_000);

        var fraisScolarite = new FluxArgent(
                "Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27),
                LocalDate.of(2024, AUGUST, 27), -200_000, 27);

        var compteBancaire = new Argent("Compte bancaire", au03juillet24, 100_000);
        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte", compteBancaire, au03juillet24.withDayOfMonth(25),
                LocalDate.of(2024, DECEMBER, 25), -20_000, 25);

        var patrimoineZetyAu17septembre24 = new Patrimoine(
                "patrimoineZetyAu17septembre24",
                zety,
                au03juillet24,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte));

        var fluxArgentPret = new FluxArgent(
                "Prêt bancaire", compteBancaire, au18septembre24, au18septembre24.plusYears(1), 10_000_000, 18);
        var dette = new Dette("Dette bancaire", au18septembre24, -11_000_000);

        var patrimoineZetyAu18septembre24 = new Patrimoine(
                "patrimoineZetyAu18septembre24",
                zety,
                au03juillet24,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte, fluxArgentPret, dette));

        int valeurPatrimoine17septembre = patrimoineZetyAu17septembre24.projectionFuture(au17septembre24).getValeurComptable();
        int valeurPatrimoine18septembre = patrimoineZetyAu18septembre24.projectionFuture(au18septembre24).getValeurComptable();
        int diminutionValeur = valeurPatrimoine18septembre - valeurPatrimoine17septembre;

        assertEquals(-1002384, diminutionValeur);
    }
}
