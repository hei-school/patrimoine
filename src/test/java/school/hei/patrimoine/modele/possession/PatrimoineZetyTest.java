package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.JULY;
import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class PatrimoineZetyTest {
    @Test
    void patrimoine_zety_le_17_septembre_2024() {
        Personne zety = new Personne("Zety");
        LocalDate au3juillet24 = LocalDate.of(2024, JULY, 3);
        LocalDate au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

        Materiel ordinateur = new Materiel(
                "Ordinateur",
                au3juillet24,
                1_200_000,
                au3juillet24.minusDays(2),
                -0.10);
        Materiel vetements = new Materiel(
                "Vêtements",
                au3juillet24,
                1_500_000,
                au3juillet24.minusDays(2),
                -0.50);
        Argent argentEspeces = new Argent("Espèces", au3juillet24, 800_000);

        FluxArgent fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                au3juillet24,
                au17septembre24,
                -200_000,
                30);

        Argent compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
        FluxArgent fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                au3juillet24,
                au17septembre24.plusDays(1000),
                -20_000,
                30);

        Patrimoine patrimoineZety = new Patrimoine(
                "patrimoineZetyAu3juillet24",
                zety,
                au17septembre24,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte));

        int valeurFuturePatrimoine = patrimoineZety.projectionFuture(au17septembre24).getValeurComptable();
        int valeurAttendue = 2978848;

        assertEquals(valeurAttendue, valeurFuturePatrimoine);
    }
}