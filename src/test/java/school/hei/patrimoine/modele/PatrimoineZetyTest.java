package school.hei.patrimoine.modele;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.devise.Devise;
import school.hei.patrimoine.modele.devise.TauxDeChange;
import school.hei.patrimoine.modele.possession.*;

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

        log.info("Patrimoine de Zety le 17 septembre 2024: " + valeurComptablePatrimoineZetyAu17Septembre24 + "Ar\n");

        assertTrue(valeurComptablePatrimoineZetyAu3juillet24 > valeurComptablePatrimoineZetyAu17Septembre24);
    }

    @Test
    public void diminution_du_patrimoine_de_zety_entre_17_et_18_septembre_2024() {
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
        var au18Septembre24 = au17Septembre24.plusDays(1);

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
                        new FluxArgent(
                                "Dette en banque",
                                argentEnBanqueAu3Juillet24,
                                au18Septembre24,
                                au18Septembre24,
                                10_000_000,
                                18
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
                        ),
                        new Dette(
                                "Dette en banque",
                                au18Septembre24,
                                -11_000_000
                        )
                )
        );

        var valeurComptablePatrimoineZetyAu17Septembre24 = patrimoineZetyAu3juillet24
                .projectionFuture(au17Septembre24)
                .getValeurComptable();
        var valeurComptablePatrimoineZetyAu18Septembre24 = patrimoineZetyAu3juillet24
                .projectionFuture(au18Septembre24)
                .getValeurComptable();
        var difference = valeurComptablePatrimoineZetyAu17Septembre24 - valeurComptablePatrimoineZetyAu18Septembre24;
        log.info("Patrimoine de Zety le 18 Septembre 2024: " + valeurComptablePatrimoineZetyAu18Septembre24 + "Ar\n" +
                "      Valeur de diminution de patrimoine de Zety le 18 Septembre 2024: " + difference + "Ar \n");

        assertTrue(valeurComptablePatrimoineZetyAu18Septembre24 < valeurComptablePatrimoineZetyAu17Septembre24);
    }

    @Test
    public void date_ou_zety_na_plus_despeces() {
        var au3Juillet24 = LocalDate.of(2024, JULY, 3);

        var argentEnEspecesAu3Juillet24 = new Argent(
                "Espèces",
                au3Juillet24,
                800_000
        );
        var novembre23 = LocalDate.of(2023, NOVEMBER, 1);
        var aout24 = LocalDate.of(2024, AUGUST, 28);


        var debut2024 = LocalDate.of(2024, JANUARY, 1);
        var au1octobre24 = LocalDate.of(2024, OCTOBER, 1);
        var au13fevrier25 = LocalDate.of(2025, FEBRUARY, 13);

        var ensembleDepensesEnEspèces = new GroupePossession(
                "Depenses en espèces",
                au3Juillet24,
                Set.of(
                        argentEnEspecesAu3Juillet24,
                        new FluxArgent(
                                "Frais de scolarité",
                                argentEnEspecesAu3Juillet24,
                                novembre23,
                                aout24,
                                -200_000,
                                27
                        ),
                        new FluxArgent(
                                "Don parentaux",
                                argentEnEspecesAu3Juillet24,
                                debut2024,
                                null,
                                100_000,
                                15
                        ),
                        new FluxArgent(
                                "Train de vie",
                                argentEnEspecesAu3Juillet24,
                                au1octobre24,
                                au13fevrier25,
                                -250_000,
                                1
                        )
                )
        );

        var daysToAdd = 0;
        while (argentEnEspecesAu3Juillet24.projectionFuture(au3Juillet24.plusDays(daysToAdd)).getValeurComptable() > 0) {
            daysToAdd++;
        }

        var dateDeFinEspèces = au3Juillet24.plusDays(daysToAdd);
        log.info("La date où Zety n'a plus d'espèces: " + dateDeFinEspèces + "\n");
        assertTrue(dateDeFinEspèces.isAfter(au3Juillet24));
    }

    @Test
    public void valeur_de_patrimoine_de_zety_au_14_fevrier_2025() {
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
        var au18Septembre24 = au17Septembre24.plusDays(1);
        var au21Septembre24 = LocalDate.of(2024, SEPTEMBER, 21);

        var debut2024 = LocalDate.of(2024, JANUARY, 1);
        var au1octobre24 = LocalDate.of(2024, OCTOBER, 1);
        var au13fevrier25 = LocalDate.of(2025, FEBRUARY, 13);
        var au14evrier25 = au13fevrier25.plusDays(1);
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
                        new FluxArgent(
                                "Dette en banque",
                                argentEnBanqueAu3Juillet24,
                                au18Septembre24,
                                au18Septembre24,
                                10_000_000,
                                18
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
                        ),
                        new Dette(
                                "Dette en banque",
                                au18Septembre24,
                                -11_000_000
                        ),
                        new FluxArgent(
                                "Don parentaux",
                                argentEnEspecesAu3Juillet24,
                                debut2024,
                                null,
                                100_000,
                                15
                        ),
                        new FluxArgent(
                                "Frais de scolarité 2024-2025",
                                argentEnBanqueAu3Juillet24,
                                au21Septembre24,
                                au21Septembre24,
                                -2_500_000,
                                21
                        ),
                        new FluxArgent(
                                "Train de vie",
                                argentEnEspecesAu3Juillet24,
                                au1octobre24,
                                au13fevrier25,
                                -250_000,
                                1
                        )
                )
        );

        var valeurComptablePatrimoineZetyAu14fervier25 = patrimoineZetyAu3juillet24
                .projectionFuture(au14evrier25)
                .getValeurComptable();
        log.info("Patrimoine de Zety le 14 Fevrier 2025: " + valeurComptablePatrimoineZetyAu14fervier25 + "Ar\n");
    }

    @Test
    public void valeur_du_patrimoine_de_zety_le_26_octobre_2025_en_euro() {
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
        var au18Septembre24 = au17Septembre24.plusDays(1);
        var au21Septembre24 = LocalDate.of(2024, SEPTEMBER, 21);

        var debut2024 = LocalDate.of(2024, JANUARY, 1);
        var au1octobre24 = LocalDate.of(2024, OCTOBER, 1);
        var au13fevrier25 = LocalDate.of(2025, FEBRUARY, 13);
        var au14evrier25 = au13fevrier25.plusDays(1);
        var au15fevrier25 = au14evrier25.plusDays(1);

        var au26octobre25 = LocalDate.of(2025, OCTOBER, 26);

        var ariary = new Devise(
                "ariary"
        );
        var euro = new Devise(
                "euro"
        );
        euro.addTauxDeChange(ariary, new TauxDeChange(4021, au3Juillet24));
        ariary.addTauxDeChange(euro, new TauxDeChange(euro.from(1, ariary, au3Juillet24, -0.10), au3Juillet24));

        var argentAuDeutscheBank = new Argent(
                "Deutsche bank",
                au15fevrier25,
                (int) ariary.from(7_000, euro, au15fevrier25, -0.10)
        );


        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineZetyAu13mai24",
                zety,
                au3Juillet24,
                Set.of(
                        argentAuDeutscheBank,
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
                        new FluxArgent(
                                "Dette en banque",
                                argentEnBanqueAu3Juillet24,
                                au18Septembre24,
                                au18Septembre24,
                                10_000_000,
                                18
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
                        ),
                        new Dette(
                                "Dette en banque",
                                au18Septembre24,
                                -11_000_000
                        ),
                        new FluxArgent(
                                "Don parentaux",
                                argentEnEspecesAu3Juillet24,
                                debut2024,
                                null,
                                100_000,
                                15
                        ),
                        new FluxArgent(
                                "Frais de scolarité 2024-2025",
                                argentEnBanqueAu3Juillet24,
                                au21Septembre24,
                                au21Septembre24,
                                -2_500_000,
                                21
                        ),
                        new FluxArgent(
                                "Train de vie",
                                argentEnEspecesAu3Juillet24,
                                au1octobre24,
                                au13fevrier25,
                                -250_000,
                                1
                        )
                )
        );

        var valeurComptablePatrimoineZetyAu26octobre25 = patrimoineZetyAu3juillet24
                .valeurComptableFuture(
                        au26octobre25,
                        ariary,
                        euro,
                        -0.10
                );
        log.info("Patrimoine de Zety le 25 Octobre 2025: " + valeurComptablePatrimoineZetyAu26octobre25 + " €\n");

        assertTrue(valeurComptablePatrimoineZetyAu26octobre25 > 7000);
    }
}
