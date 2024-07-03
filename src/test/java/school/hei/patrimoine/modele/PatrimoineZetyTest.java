package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyTest {
    @Test
    void patrimoine_de_zety() {
        var Zety = new Personne("Zety");
        var au03juillet2024 = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel("ordinateur", au03juillet2024, 1_200_000, au03juillet2024, -0.10);

        var vetements = new Materiel("vêtements", au03juillet2024, 1_500_000, au03juillet2024, -0.50);

        var argentEspeces = new Argent("argent en espèces", au03juillet2024, 800_000);

        int fraisScolariteTotal = 0;
        LocalDate debutScolarite = LocalDate.of(2024, JULY, 27);
        LocalDate finScolarite = LocalDate.of(2024, AUGUST, 27);
        LocalDate datePaiement = debutScolarite;
        while (!datePaiement.isAfter(finScolarite)) {
            fraisScolariteTotal += 200_000;
            datePaiement = datePaiement.plusMonths(1);
        }
        var fraisScolarite = new Argent("frais de scolarité", au03juillet2024, fraisScolariteTotal);

        int fraisTenueCompte = 0;
        LocalDate debutCompte = LocalDate.of(2024, JULY, 25);
        LocalDate datePonction = debutCompte;
        while (!datePonction.isAfter(LocalDate.of(2024, SEPTEMBER, 17))) {
            fraisTenueCompte += 20_000;
            datePonction = datePonction.plusMonths(1);
        }

        var compteBancaire = new Argent("compte bancaire", au03juillet2024, 100_000 - fraisTenueCompte);

        var au17septembre2024 = LocalDate.of(2024, SEPTEMBER, 17);

        var patrimoine_de_zety = new Patrimoine(
                "patrimoineDeZety",
                Zety, au17septembre2024,
                Set.of(
                        ordinateur, vetements, compteBancaire, fraisScolarite
                ));

        assertEquals(2_978_848, patrimoine_de_zety.projectionFuture(au17septembre2024).getValeurComptable());



    }


    @Test
    void patrimoine_de_zety_en_dette() {
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

        var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
        var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
        var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);

        var fraisScolarite = new FluxArgent(
                "Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27),
                LocalDate.of(2024, AUGUST, 27), -200_000, 27);

        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte", compteBancaire, au3juillet24.withDayOfMonth(25),
                LocalDate.of(2024, DECEMBER, 25), -20_000, 25);

        var patrimoineDeZetyAu17septembre24 = new Patrimoine(
                "patrimoineDeZetyAu17septembre24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte));

        var fluxArgentPret = new FluxArgent(
                "Prêt bancaire", compteBancaire, au18septembre24, au18septembre24.plusYears(1), 10_000_000, 18);
        var dette = new Dette("Dette bancaire", au18septembre24, -11_000_000);

        var patrimoineDeZetyAu18septembre24 = new Patrimoine(
                "patrimoineDeZetyAu18septembre24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte, fluxArgentPret, dette));

        int patrimoinePour17Septembre = patrimoineDeZetyAu17septembre24.projectionFuture(au17septembre24).getValeurComptable();
        int patrimoinePour18Septembre = patrimoineDeZetyAu18septembre24.projectionFuture(au18septembre24).getValeurComptable();
        int valeurDeDette = patrimoinePour18Septembre - patrimoinePour17Septembre ;

        assertEquals(-1002384, valeurDeDette);
    }
}
