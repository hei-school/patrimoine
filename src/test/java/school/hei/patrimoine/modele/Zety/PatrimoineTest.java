package school.hei.patrimoine.modele.Zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;

import java.time.LocalDate;
import java.util.Set;

import static java.time.LocalDate.now;
import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineTest {
    @Test
    // Test patrimoine zety 17-09-24
    void patrimoine_zety(){
        var zety = new Personne("Zety");
        var au17Sept24 = LocalDate.of(2024, SEPTEMBER, 17);

        // Définir les montants réels d'espèces et de compte bancaire de Zety le 17 septembre 2024
        var montantEspece = 400_000;
        var montantCompteBancaire = 40_000;

        var patrimoineZetyAu17Sept24 = new Patrimoine(
                "patrimoineZetyAu17Sept24",
                zety,
                au17Sept24,
                Set.of(
                        new Argent("Espèces", au17Sept24, montantEspece),
                        new Argent("Compte bancaire", au17Sept24, montantCompteBancaire))
        );
        assertEquals(440000, patrimoineZetyAu17Sept24.getValeurComptable());
    }


}
