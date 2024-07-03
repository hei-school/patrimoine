package school.hei.patrimoine.modele.PatrimoineZetyTest;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmpruntTest {
    @Test
    void patrimoine_de_zety_apres_emprunt() {

        var au17Septembre = LocalDate.of(2024, 9, 17);
        var ordinateur = new Materiel("Ordinateur", LocalDate.of(2024, 7, 3), 1_200_000, LocalDate.of(2024, 7, 3), -0.10);
        var vetements = new Materiel("Vêtements", LocalDate.of(2024, 7, 3), 1_500_000, LocalDate.of(2024, 7, 3), -0.50);
        var argentEnEspece = new Argent("Argent en espèces", LocalDate.of(2024, 7, 3), 800_000);
        var compteBancaire = new Argent("Compte bancaire", LocalDate.of(2024, 7, 3), 100_000);
        var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, LocalDate.of(2024, 7, 3), LocalDate.of(2024, 7, 3).plusMonths(12), -20_000, 25);
        var fraisScolarite = new Argent("Frais de scolarité", LocalDate.of(2024, 7, 3), 0);
        var fluxFraisScolarite = new FluxArgent("Frais de scolarité", fraisScolarite, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);

        var patrimoineZety = new Patrimoine("Patrimoine Zety", new Personne("Zety"), LocalDate.of(2024, 7, 3), Set.of(ordinateur, vetements, argentEnEspece, compteBancaire, fraisScolarite, fluxFraisScolarite, fraisTenueCompte));


        var projectionAu17Septembre = patrimoineZety.projectionFuture(au17Septembre);
        assertEquals(2978848, projectionAu17Septembre.getValeurComptable());
        var au18Septembre = LocalDate.of(2024, 9, 18);
        var dette = new Dette("Dette bancaire", au18Septembre, -11_000_000); // Création de la dette
        var fluxDette = new FluxArgent("Emprunt bancaire", compteBancaire, au18Septembre, LocalDate.of(2025, 9, 18), 10_000_000, 18);
        var patrimoineZetyEndette = new Patrimoine("Patrimoine Zety endetté", new Personne("Zety"), LocalDate.of(2024, 7, 3), Set.of(ordinateur, vetements, argentEnEspece, compteBancaire, fraisScolarite, fluxFraisScolarite, fraisTenueCompte, dette, fluxDette));
        var projectionAu18Septembre = patrimoineZetyEndette.projectionFuture(au18Septembre);

        var diminutionPatrimoine = projectionAu17Septembre.getValeurComptable() - projectionAu18Septembre.getValeurComptable();

        assertEquals(1_002_384, diminutionPatrimoine);
    }
}
