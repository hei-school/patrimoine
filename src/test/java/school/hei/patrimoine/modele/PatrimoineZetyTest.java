package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyTest {
    @Test
    void valeur_patrimoine_de_zety_au_17sept2024(){
        var zety = new Personne("zety");
        var aujuillet24 = LocalDate.of(2024, Month.JULY, 3);

        var ordinateur = new Materiel(
                "ordinateur",
                aujuillet24,
                1_200_000,
                aujuillet24.minusDays(1),
                -0.10);
        var vetement = new Materiel(
                "vetement",
                aujuillet24,
                1_500_000,
                aujuillet24.minusDays(1),
                -0.50);
        var argentEnEspeces = new Argent(
                "Especes",
                aujuillet24,
                800_000);
        var fraisDeScolarite = new FluxArgent(
                "frais de scolarité",
                argentEnEspeces,
                LocalDate.of(2023, Month.NOVEMBER,27),
                LocalDate.of(2024, Month.AUGUST, 27),
                -200_000,
                1);
        var compteBancaire = new Argent(
                "Compte bancaire",
                aujuillet24,
                100_000);
        var fraisDeTenueDeCompte = new FluxArgent(
                "frais de tenue de compte",
                compteBancaire,
                aujuillet24.minusDays(1),
                LocalDate.MAX,
                -200_000,
                1);
        var zetyPatrimoine = new Patrimoine(
                "patrimoine de zety",
                zety,
                aujuillet24,
                Set.of(ordinateur, vetement, argentEnEspeces,
                        fraisDeScolarite, compteBancaire,fraisDeTenueDeCompte));
        var au17sept24 = LocalDate.of(2024, Month.SEPTEMBER, 17);
        var valeurAuFutur = zetyPatrimoine.projectionFuture(au17sept24).getValeurComptable();

        assertEquals(2_818_848, valeurAuFutur);
    }
}
