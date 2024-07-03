package school.hei.patrimoine.PatrimoineDeZety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestZety {

    @Test
    void valeurPatrimoineAu17Septembre2024() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, 7, 3);
        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet2024,
                1200000,
                au3juillet2024.minusDays(2),
                -0.10);

        var vetements = new Materiel(
                "Vêtements",
                au3juillet2024,
                1500000,
                au3juillet2024.minusDays(2),
                -0.50);

        var argentEspeces = new Argent("Espèces", au3juillet2024, 800000);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                LocalDate.of(2023, 11, 27),
                LocalDate.of(2024, 8, 27),
                -200000,
                30);

        var compteBancaire = new Argent("Compte bancaire", au3juillet2024, 100000);

        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                au3juillet2024.minusMonths(1),
                au3juillet2024.plusYears(1),
                -20000,
                25);

        var patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte));

        var valeurPatrimoineAu17Septembre2024 = patrimoineZety.projectionFuture(LocalDate.of(2024, 9, 17)).getValeurComptable();
        var valeurOrdinateurAu17Septembre2024 = ordinateur.valeurComptableFuture(LocalDate.of(2024, 9, 17));
        var valeurVetementsAu17Septembre2024 = vetements.valeurComptableFuture(LocalDate.of(2024, 9, 17));
        var valeurEspecesAu17Septembre2024 = argentEspeces.valeurComptableFuture(LocalDate.of(2024, 9, 17));
        var valeurCompteBancaireAu17Septembre2024 = compteBancaire.valeurComptableFuture(LocalDate.of(2024, 9, 17));

        var valeurTotaleAttendue = valeurOrdinateurAu17Septembre2024 + valeurVetementsAu17Septembre2024 + valeurEspecesAu17Septembre2024 + valeurCompteBancaireAu17Septembre2024;
        assertEquals(valeurTotaleAttendue, valeurPatrimoineAu17Septembre2024);

        System.out.println("la valeur comptable de zety sera : " + valeurTotaleAttendue);
    }
}
