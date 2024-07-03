package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineZetyTest {

    @Test
    void valeur_patrimoine_zety_le_17_septembre_2024() {
        var ordinateur = new Materiel("Ordinateur", LocalDate.of(2024, 7, 3), 1_200_000, LocalDate.of(2024, 7, 3), -0.1);
        var vetements = new Materiel("Vêtements", LocalDate.of(2024, 7, 3), 1_500_000, LocalDate.of(2024, 7, 3), -0.5);
        var argentEspeces = new Argent("Espèces", LocalDate.of(2024, 7, 3), 800_000);
        var fraisScolarite = new FluxArgent("Frais scolarité", argentEspeces, LocalDate.of(2024, 7, 3), LocalDate.of(2024, 9, 17), -200_000, 1);
        var compteBancaire = new Argent("Compte bancaire", LocalDate.of(2024, 7, 3), 100_000);
        var fraisCompteBancaire = new FluxArgent("Frais compte bancaire", compteBancaire, LocalDate.of(2024, 7, 3), LocalDate.of(2024, 12, 31), -20_000, 1);

        var personne = new Personne("Zety");

        var patrimoineZety = new Patrimoine("Zety", personne, LocalDate.of(2024, 7, 3), Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisCompteBancaire));

        var dateProjection = LocalDate.of(2024, 9, 17);
        Patrimoine patrimoineFutur = patrimoineZety.projectionFuture(dateProjection);
        int valeurFuture = patrimoineFutur.getValeurComptable();

        assertEquals(2978848, valeurFuture);
    }

    @Test
    void diminution_patrimoine_zety_entre_17_et_18_septembre_2024() {
        var au3juillet24 = LocalDate.of(2024, JULY, 3);

        var zety = new Personne("Zety");
        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet24,
                1_200_000,
                au3juillet24,
                -0.10);

        var vetements = new Materiel(
                "Vêtements",
                au3juillet24,
                1_500_000,
                au3juillet24,
                -0.50);

        var argentEspece = new Argent("Espèces", au3juillet24, 800_000);

        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspece,
                LocalDate.of(2023, NOVEMBER, 27),
                LocalDate.of(2024, SEPTEMBER, 1),
                -200_000,
                1);

        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);

        var patrimoineZetyAu3juillet2024 = new Patrimoine(
                "Patrimoine de Zety au 3 juillet 2024",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, argentEspece, fraisScolarite, compteBancaire));

        var detteBancaire = new Dette("Dette bancaire", LocalDate.of(2024, SEPTEMBER, 18), -11_000_000);
        var possessionsAu18septembre2024 = Set.of(ordinateur, vetements, argentEspece, fraisScolarite, compteBancaire, detteBancaire);
        var patrimoineZetyAu18septembre24 = new Patrimoine(
                "Patrimoine de Zety au 18 septembre 2024",
                zety,
                LocalDate.of(2024, SEPTEMBER, 18),
                possessionsAu18septembre2024);

        var valeurPatrimoineAu17septembre2024 = patrimoineZetyAu3juillet2024.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable();
        var valeurPatrimoineAu18septembre2024 = patrimoineZetyAu18septembre24.getValeurComptable();
        int diminutionPatrimoine = valeurPatrimoineAu18septembre2024 - valeurPatrimoineAu17septembre2024;

        assertEquals(-10_418_848, diminutionPatrimoine);

    }
}
