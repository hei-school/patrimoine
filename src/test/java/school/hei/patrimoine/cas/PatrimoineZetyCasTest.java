package school.hei.patrimoine.cas;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Dette;
import java.time.LocalDate;
import java.util.Set;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.time.Month.AUGUST;
import static java.time.Month.NOVEMBER;
import static java.time.Month.SEPTEMBER;
import static java.time.Month.JULY;
import static java.time.Month.OCTOBER;

public class PatrimoineZetyCasTest {

    private static final Logger logger = Logger.getLogger(PatrimoineZetyCasTest.class.getName());

    @Test
    public void testValeurPatrimoine() {
        var zety = new Personne("Zety");
        var dateDebut = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel("Ordinateur", dateDebut, 1_200_000, dateDebut, -0.10);
        var vetements = new Materiel("Vêtements", dateDebut, 1_500_000, dateDebut, -0.50);
        var argentLiquide = new Argent("Argent liquide", dateDebut, 800_000);
        var compteBancaire = new Argent("Compte bancaire", dateDebut, 100_000);

        new FluxArgent("Frais de scolarité", argentLiquide, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200_000, 27);
        new FluxArgent("Frais bancaires", compteBancaire, dateDebut, LocalDate.of(2024, SEPTEMBER, 17), -20_000, 25);

        var possessions = Set.of(ordinateur, vetements, argentLiquide, compteBancaire);

        var patrimoineZety = new Patrimoine("Patrimoine de Zety", zety, dateDebut, possessions);

        LocalDate dateFutur = LocalDate.of(2024, SEPTEMBER, 17);
        Patrimoine patrimoineFutur = patrimoineZety.projectionFuture(dateFutur);

        int valeurComptable = patrimoineFutur.getValeurComptable();
        logger.info("Valeur comptable du patrimoine de Zety au 17 septembre 2024: " + valeurComptable + " Ar");

        // Utiliser la valeur exacte fournie
        int expectedValeurComptable = 2_978_848;
        assertEquals(expectedValeurComptable, valeurComptable);
    }

    @Test
    public void testValeurPatrimoineAvecDette() {
        var zety = new Personne("Zety");
        var dateDebut = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel("Ordinateur", dateDebut, 1_200_000, dateDebut, -0.10);
        var vetements = new Materiel("Vêtements", dateDebut, 1_500_000, dateDebut, -0.50);
        var argentLiquide = new Argent("Argent liquide", dateDebut, 800_000);
        var compteBancaire = new Argent("Compte bancaire", dateDebut, 100_000);

        new FluxArgent("Frais de scolarité", argentLiquide, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200_000, 27);
        new FluxArgent("Frais bancaires", compteBancaire, dateDebut, LocalDate.of(2024, SEPTEMBER, 17), -20_000, 25);

        // Ajouter la dette
        var dateDette = LocalDate.of(2024, SEPTEMBER, 18);
        var montantDette = -10_000_000; // Valeur négative pour la dette
        var dette = new Dette("Prêt bancaire", dateDette, montantDette);

        var possessions = Set.of(ordinateur, vetements, argentLiquide, compteBancaire, dette);

        var patrimoineZety = new Patrimoine("Patrimoine de Zety", zety, dateDebut, possessions);

        LocalDate dateAvantDette = LocalDate.of(2024, SEPTEMBER, 17);
        LocalDate dateApresDette = LocalDate.of(2024, SEPTEMBER, 18);

        Patrimoine patrimoineAvantDette = patrimoineZety.projectionFuture(dateAvantDette);
        Patrimoine patrimoineApresDette = patrimoineZety.projectionFuture(dateApresDette);

        int valeurComptableAvantDette = patrimoineAvantDette.getValeurComptable();
        int valeurComptableApresDette = patrimoineApresDette.getValeurComptable();

        int expectedValeurComptableApresDette = valeurComptableAvantDette + montantDette;

        logger.info("Valeur comptable du patrimoine de Zety au 17 septembre 2024: " + valeurComptableAvantDette + " Ar");
        logger.info("Valeur comptable du patrimoine de Zety au 18 septembre 2024: " + valeurComptableApresDette + " Ar");

        assertEquals(expectedValeurComptableApresDette, valeurComptableApresDette);
    }

    @Test
    public void testValeurPatrimoineAvecManqueDEspeces() {
        var zety = new Personne("Zety");
        var dateDebut = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel("Ordinateur", dateDebut, 1_200_000, dateDebut, -0.10);
        var vetements = new Materiel("Vêtements", dateDebut, 1_500_000, dateDebut, -0.50);
        var argentLiquide = new Argent("Argent liquide", dateDebut, 800_000);
        var compteBancaire = new Argent("Compte bancaire", dateDebut, 100_000);

        new FluxArgent("Frais bancaires", compteBancaire, dateDebut, LocalDate.of(2024, SEPTEMBER, 17), -20_000, 25);
        // Ajouter la dette
        var dateDette = LocalDate.of(2024, SEPTEMBER, 18);
        var montantDette = -10_000_000; // Valeur négative pour la dette
        var dette = new Dette("Prêt bancaire", dateDette, montantDette);

        var possessions = Set.of(ordinateur, vetements, argentLiquide, compteBancaire, dette);

        var patrimoineZety = new Patrimoine("Patrimoine de Zety", zety, dateDebut, possessions);

        LocalDate dateManqueDEspeces = LocalDate.of(2024, OCTOBER, 1);

        Patrimoine patrimoineManqueDEspeces = patrimoineZety.projectionFuture(dateManqueDEspeces);

        int valeurComptableManqueDEspeces = patrimoineManqueDEspeces.getValeurComptable();

        logger.info("Valeur comptable du patrimoine de Zety au 1 octobre 2024: " + valeurComptableManqueDEspeces + " Ar");

        // Calculer la valeur attendue en prenant en compte le manque d'espèces
        int expectedValeurComptableManqueDEspeces = 2_978_848 + montantDette;

        assertEquals(expectedValeurComptableManqueDEspeces, valeurComptableManqueDEspeces);
    }
}