package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatrimoineZetyTest {

    private Personne zety;
    private LocalDate au3Juillet2024;
    private LocalDate au17Septembre2024;

    @BeforeEach
    void setUp() {
        zety = new Personne("Zety");
        au3Juillet2024 = LocalDate.of(2024, Month.JULY, 3);
        au17Septembre2024 = LocalDate.of(2024, Month.SEPTEMBER, 17);
    }

    @Test
    void patrimoine17septembre2024Test() {
        Materiel ordinateur = new Materiel("Ordinateur", au3Juillet2024, 1_200_000, au3Juillet2024.minusDays(2), -0.10);
        Materiel vetements = new Materiel("Vêtements", au3Juillet2024, 1_500_000, au3Juillet2024.minusDays(2), -0.50);
        Argent argentEspeces = new Argent("Espèces", au3Juillet2024, 800_000);
        FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité", argentEspeces, au3Juillet2024, au17Septembre2024, -200_000, 30);
        Argent compteBancaire = new Argent("Compte bancaire", au3Juillet2024, 100_000);
        FluxArgent fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, au3Juillet2024, au17Septembre2024.plusDays(1000), -20_000, 30);

        Patrimoine patrimoineZety = new Patrimoine("patrimoineZetyAu3juillet24", zety, au17Septembre2024,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte));

        int valeurFuturePatrimoine = patrimoineZety.projectionFuture(au17Septembre2024).getValeurComptable();
        int valeurAttendue = 2_978_848;
        assertEquals(valeurAttendue, valeurFuturePatrimoine);
    }

    @Test
    void testDiminutionPatrimoineZety() {
        Materiel ordinateur = new Materiel("Ordinateur", au3Juillet2024, 1_200_000, au3Juillet2024, -0.10);
        Materiel vetements = new Materiel("Vêtements", au3Juillet2024, 1_500_000, au3Juillet2024, -0.50);
        Argent espece = new Argent("Espèce", au3Juillet2024, 800_000);
        LocalDate debutScolarite = LocalDate.of(2023, Month.NOVEMBER, 1);
        LocalDate finScolarite = LocalDate.of(2024, Month.AUGUST, 30);
        FluxArgent fraisDeScolarite = new FluxArgent("Frais de Scolarité", espece, debutScolarite, finScolarite, -200_000, 27);

        Argent compteBancaire = new Argent("CompteBancaire", au3Juillet2024, 100_000);
        FluxArgent fraisDuCompte = new FluxArgent("Frais de compte", compteBancaire, au3Juillet2024, LocalDate.MAX, -20_000, 25);

        LocalDate dateEmprunt = LocalDate.of(2024, Month.SEPTEMBER, 18);
        Dette dette = new Dette("Dette Scolarité", au3Juillet2024, 0);

        FluxArgent pret = new FluxArgent("Frais De Scolarité Prêt", compteBancaire, dateEmprunt, dateEmprunt, 10_000_000, dateEmprunt.getDayOfMonth());
        FluxArgent detteAjout = new FluxArgent("Frais De Scolarité Dette", dette, dateEmprunt, dateEmprunt, -11_000_000, dateEmprunt.getDayOfMonth());
        FluxArgent remboursement = new FluxArgent("Frais De Scolarité Rem", compteBancaire, dateEmprunt.plusYears(1), dateEmprunt.plusYears(1), -11_000_000, dateEmprunt.plusYears(1).getDayOfMonth());
        FluxArgent detteAnnulation = new FluxArgent("Frais De Scolarité annulation", dette, dateEmprunt.plusYears(1), dateEmprunt.plusYears(1), 11_000_000, dateEmprunt.plusYears(1).getDayOfMonth());

        GroupePossession detteCompteBancaire = new GroupePossession("Compte Bancaire", au3Juillet2024, Set.of(pret, detteAjout, remboursement, detteAnnulation));

        Patrimoine patrimoineZetyAu3juillet24 = new Patrimoine("patrimoineZetyAu3juillet24", zety, au3Juillet2024,
                Set.of(ordinateur, vetements, espece, fraisDeScolarite, compteBancaire, fraisDuCompte, dette, detteCompteBancaire));

        LocalDate au17Septembre2024 = LocalDate.of(2024, Month.SEPTEMBER, 17);
        LocalDate au18Septembre2024 = LocalDate.of(2024, Month.SEPTEMBER, 18);
        assertTrue(patrimoineZetyAu3juillet24.projectionFuture(au17Septembre2024).getValeurComptable() > patrimoineZetyAu3juillet24.projectionFuture(au18Septembre2024).getValeurComptable());
        assertEquals(1_002_384, patrimoineZetyAu3juillet24.projectionFuture(au17Septembre2024).getValeurComptable() - patrimoineZetyAu3juillet24.projectionFuture(au18Septembre2024).getValeurComptable());
    }
    @Test
    void zetySpecesApres18Septembre2024Test() {
        Argent argentEspeces = new Argent("Espèces", au3Juillet2024, 800_000);
        Materiel ordinateur = new Materiel("Ordinateur", au3Juillet2024, 1_200_000, au3Juillet2024.minusDays(2), -0.10);
        Materiel vetements = new Materiel("Vêtements", au3Juillet2024, 1_500_000, au3Juillet2024.minusDays(2), -0.50);

        FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité", argentEspeces, LocalDate.of(2023, Month.NOVEMBER, 27), LocalDate.of(2024, Month.AUGUST, 27), -200_000, 30);

        Argent compteBancaire = new Argent("Compte bancaire", au3Juillet2024, 100_000);
        FluxArgent fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, au3Juillet2024.minusMonths(1), LocalDate.of(2025, Month.JANUARY, 1), -20_000, 30);

        Patrimoine patrimoineZety = new Patrimoine("Patrimoine de Zety", zety, au3Juillet2024,
                Set.of(argentEspeces, ordinateur, vetements, fraisScolarite, compteBancaire, fraisTenueCompte));

        LocalDate dateProjection = au3Juillet2024;
        while (true) {
            if (dateProjection.equals(LocalDate.of(2024, Month.SEPTEMBER, 21))) {
                argentEspeces = new Argent("Espèces", dateProjection, argentEspeces.valeurComptable - 2_500_000);
            }
            if (dateProjection.getDayOfMonth() == 15 && dateProjection.getMonthValue() >= 1 && dateProjection.getYear() == 2024) {
                argentEspeces = new Argent("Espèces", dateProjection, argentEspeces.valeurComptable + 100_000);
            }
            if (dateProjection.getMonthValue() >= 10 && dateProjection.getDayOfMonth() == 1 &&
                    dateProjection.isBefore(LocalDate.of(2025, Month.FEBRUARY, 14))) {
                argentEspeces = new Argent("Espèces", dateProjection, argentEspeces.valeurComptable - 250_000);
            }
            if (argentEspeces.valeurComptable <= 0) {
                break;
            }
            dateProjection = dateProjection.plusDays(1);
        }
        assertEquals(LocalDate.of(2024, Month.SEPTEMBER, 21), dateProjection);
    }

}
