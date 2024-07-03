package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyCasTest {
    @Test
    void patrimoine_de_zety_le_17_septembre_2024() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, JULY, 3);
        var ordinateur = new Materiel("Ordinateur", au3juillet2024, 1200000, au3juillet2024.minusDays(10), -0.1);
        var vetements = new Materiel("Vetements", au3juillet2024, 1500000, au3juillet2024.minusDays(1000), -0.5);
        var argentEspece = new Argent("Especes", au3juillet2024, 800000);
        var fraisScolarite = new FluxArgent("Frais de scolarite", argentEspece, LocalDate.of(2023, NOVEMBER, 1), LocalDate.of(2024, AUGUST, 31), -200000, 27);
        var argentCompte = new Argent("Espèces", LocalDate.of(2024, JULY, 3), 100000);
        LocalDate dateFinIndefinie = LocalDate.of(9999, DECEMBER, 31);
        var fraisDeTenueDeCompte = new FluxArgent("Frais de tenue de compte", argentEspece,au3juillet2024.minusDays(2), dateFinIndefinie, -20000, 25);
        var patrimoineZety=  new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(ordinateur, vetements, argentCompte, argentEspece, fraisScolarite, fraisDeTenueDeCompte)
        );
        var dateDeProjection  = LocalDate.of(2024,SEPTEMBER,17);
        assertEquals(2978848,patrimoineZety.projectionFuture(dateDeProjection).getValeurComptable());

    }
}