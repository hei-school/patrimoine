package school.hei.patrimoine.PatrimoineDeZety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDiminutionPatrimoineZety {

    @Test
    // dimunition de la valeur du  patrimoine de Zety entre le 17 et le 18 septembre 2024
    void diminutionPatrimoineEntre17Et18Septembre2024() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, Month.JULY, 3);
        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet2024,
                1_200_000,
                au3juillet2024.minusDays(2),
                -0.10);

        var vetements = new Materiel(
                "Vêtements",
                au3juillet2024,
                1_500_000,
                au3juillet2024.minusDays(2),
                -0.50);

        var argentEspeces = new Argent("Espèces", au3juillet2024, 800_000);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                LocalDate.of(2023, Month.NOVEMBER, 27),
                LocalDate.of(2024, Month.AUGUST, 27),
                -200_000,
                30);

        var compteBancaire = new Argent("Compte bancaire", au3juillet2024, 100_000);

        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                au3juillet2024.minusMonths(1),
                au3juillet2024.plusYears(1),
                -20_000,
                30);

        var patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte));
        var valeurPatrimoineAu17Septembre2024 = patrimoineZety.projectionFuture(LocalDate.of(2024, Month.SEPTEMBER, 17)).getValeurComptable();
        var valeurPatrimoineAu18Septembre2024 = patrimoineZety.projectionFuture(LocalDate.of(2024, Month.SEPTEMBER, 18)).getValeurComptable();
        var diminutionPatrimoine = valeurPatrimoineAu18Septembre2024 - valeurPatrimoineAu17Septembre2024;

        assertEquals(-2384, diminutionPatrimoine);
    }

}

