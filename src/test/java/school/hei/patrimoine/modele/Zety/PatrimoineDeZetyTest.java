package school.hei.patrimoine.modele.Zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineDeZetyTest {
    @Test
    void patrimoine_vaut_0() {
        var zety = new Personne("Zety");
        var patrimoineZetyAu17sept24 = new Patrimoine("patrimoineDeZetyAu17sept24",
                zety,
                LocalDate.of(2024, SEPTEMBER,17),
                Set.of());
        assertEquals(0, patrimoineZetyAu17sept24.getValeurComptable());
    }
    @Test
    void patrimoine_de_Zety_le_17_septembre_2024() {
        LocalDate au3jul24 = LocalDate.of(2024, JULY, 3);

        Materiel ordinateur = new Materiel(
                "Ordinateur",
                au3jul24,
                1_200_000,
                au3jul24,
                -0.10);

        Materiel vetements = new Materiel(
                "Vêtements",
                au3jul24,
                1_500_000,
                au3jul24,
                -0.50);

        Argent argentEspeces = new Argent(
                "Espèces",
                au3jul24,
                800_000);

        LocalDate debutFraisScolarite = LocalDate.of(2023, NOVEMBER, 27);
        LocalDate finFraisScolarite = LocalDate.of(2024, AUGUST, 27);
        FluxArgent fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                debutFraisScolarite,
                finFraisScolarite,
                200_000,
                27);

        Argent compteBancaire = new Argent(
                "Compte Bancaire",
                au3jul24,
                100_000);

        LocalDate debutCompteBancaire = LocalDate.of(2024, JULY, 25);
        FluxArgent fraisCompteBancaire = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                debutCompteBancaire,
                LocalDate.MAX,
                -20_000,
                25);

        argentEspeces.addFinancés(fraisScolarite);
        compteBancaire.addFinancés(fraisCompteBancaire);

        Patrimoine patrimoineZety = new Patrimoine(
                "patrimoineZetyLe17Sept24",
                new Personne("Zety"),
                au3jul24,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisCompteBancaire));

        patrimoineZety = patrimoineZety.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17));

        double valeurComptableAttendue =
                ordinateur.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable() +
                        vetements.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable() +
                        argentEspeces.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable() +
                        fraisScolarite.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable() +
                        compteBancaire.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable() +
                        fraisCompteBancaire.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable();

        assertEquals(2978848.0, valeurComptableAttendue);
    }
}
