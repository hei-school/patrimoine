package school.hei.patrimoine.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZetyPatrimoineEndettementTest {

    private Personne zety;
    private LocalDate au17septembre24;
    private LocalDate au18septembre24;

    @BeforeEach
    void setUp() {
        zety = new Personne("Zety");

        // Dates
        au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
        au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);
    }

    @Test
    void testDiminutionPatrimoineZetyEndettement() {
        Argent compteBancaire = new Argent("Compte bancaire", au17septembre24, 100_000);
        Dette detteBancaire = new Dette("Dette bancaire", au18septembre24, -11_000_000); // Montant négatif pour représenter la dette
        FluxArgent fraisDette = new FluxArgent(
                "Frais de la dette",
                compteBancaire,
                au18septembre24,
                au18septembre24.plusYears(1),
                -1_000_000,
                18);
        TransfertArgent transfertEndettement = new TransfertArgent(
                "Transfert endettement",
                compteBancaire, compteBancaire,
                au18septembre24,
                au18septembre24,
                10_000_000,
                18);

        Set<Possession> possessionsAu17Septembre = new HashSet<>();
        possessionsAu17Septembre.add(compteBancaire);

        Patrimoine patrimoineZetyAu17septembre24 = new Patrimoine(
                "patrimoineZetyAu17septembre24",
                zety,
                au17septembre24,
                possessionsAu17Septembre);

        Set<Possession> nouvellesPossessions = new HashSet<>(possessionsAu17Septembre);
        nouvellesPossessions.add(detteBancaire);
        nouvellesPossessions.add(fraisDette);
        nouvellesPossessions.add(transfertEndettement);

        Patrimoine patrimoineZetyAu18septembre24 = new Patrimoine(
                "patrimoineZetyAu18septembre24",
                zety,
                au18septembre24,
                nouvellesPossessions);

        int valeurPatrimoineAu17Septembre = patrimoineZetyAu17septembre24.getValeurComptable();
        assertEquals(100_000, valeurPatrimoineAu17Septembre, "La valeur comptable du patrimoine au 17 septembre 2024 est incorrecte");

        int diminutionPatrimoine = valeurPatrimoineAu17Septembre - patrimoineZetyAu18septembre24.getValeurComptable();
        System.out.println("Diminution du patrimoine de Zety entre le 17 et le 18 septembre 2024 : " + diminutionPatrimoine);

        assertEquals(11_000_000, diminutionPatrimoine, "La diminution du patrimoine de Zety est incorrecte");
    }
}
