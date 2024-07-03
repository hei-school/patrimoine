package school.hei.patrimoine.cas;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;
import java.util.logging.Logger;

import static java.time.Month.AUGUST;
import static java.time.Month.NOVEMBER;
import static java.time.Month.SEPTEMBER;
import static java.time.Month.JULY;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyCasTest {

    private static final Logger logger = Logger.getLogger(PatrimoineZetyCasTest.class.getName());

    @Test
    public void testValeurPatrimoine() {
        var zety = new Personne("Zety");
        var dateEtude = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel("Ordinateur", dateEtude, 1200000, dateEtude, -0.10);
        var vetements = new Materiel("Vêtements", dateEtude, 1500000, dateEtude, -0.50);
        var argentEspeces = new Argent("Argent en espèces", dateEtude, 800000);
        var compteBancaire = new Argent("Compte bancaire", dateEtude, 100000);

        new FluxArgent("Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200000, 27);
        new FluxArgent("Frais de tenue de compte", compteBancaire, dateEtude, LocalDate.of(2024, SEPTEMBER, 17), -20000, 25);

        var possessions = Set.of(ordinateur, vetements, argentEspeces, compteBancaire);

        var patrimoineZety = new Patrimoine("Patrimoine de Zety", zety, dateEtude, possessions);

        LocalDate dateFutur = LocalDate.of(2024, SEPTEMBER, 17);
        Patrimoine patrimoineFutur = patrimoineZety.projectionFuture(dateFutur);

        int valeurComptable = patrimoineFutur.getValeurComptable();
        logger.info("Valeur comptable du patrimoine de Zety au 17 septembre 2024: " + valeurComptable +"Ar");


        assertEquals(valeurComptable, patrimoineFutur.getValeurComptable());
    }
}