package school.hei.patrimoine.modele;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PatrimoineZetyTest {
    private static final Log log = LogFactory.getLog(PatrimoineZetyTest.class);

    @Test
    public void patrimoine_de_zety_au_17_sept_2024() {
        var zety = new Personne("Zety");
        var au3Juillet24 = LocalDate.of(2024, JULY, 3);

        var argentEnEspecesAu3Juillet24 = new Argent(
                "Espèces",
                au3Juillet24,
                800_000
        );
        var argentEnBanqueAu3Juillet24 = new Argent(
                "Compte en Banque",
                au3Juillet24,
                100_000
        );

        var novembre23 = LocalDate.of(2023, NOVEMBER, 1);
        var aout24 = LocalDate.of(2024, AUGUST, 28);

        var au17Septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineZetyAu13mai24",
                zety,
                au3Juillet24,
                Set.of(
                        argentEnEspecesAu3Juillet24,
                        argentEnBanqueAu3Juillet24,
                        new FluxArgent(
                                "Frais de scolarité",
                                argentEnEspecesAu3Juillet24,
                                novembre23,
                                aout24,
                                -200_000,
                                27
                        ),
                        new FluxArgent(
                                "Frais de tenue du compte",
                                argentEnBanqueAu3Juillet24,
                                au3Juillet24,
                                au17Septembre24,
                                -20_000,
                                25
                        ),
                        new Materiel(
                                "Ordinateur",
                                au3Juillet24,
                                1_200_000,
                                null,
                                -0.10
                        ),
                        new Materiel(
                                "Vêtements",
                                au3Juillet24,
                                1_500_000,
                                null,
                                -0.50
                        )
                )
        );

        var valeurComptablePatrimoineZetyAu3juillet24 = patrimoineZetyAu3juillet24.getValeurComptable();
        var valeurComptablePatrimoineZetyAu17Septembre24 = patrimoineZetyAu3juillet24
                .projectionFuture(au17Septembre24)
                .getValeurComptable();

        log.info("Patrimoine de Zety le 17 septembre 2024: " + valeurComptablePatrimoineZetyAu17Septembre24 + "Ar");

        assertTrue(valeurComptablePatrimoineZetyAu3juillet24 > valeurComptablePatrimoineZetyAu17Septembre24);
    }
}
