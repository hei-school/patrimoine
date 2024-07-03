package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;

import static java.time.Month.AUGUST;
import static java.time.Month.NOVEMBER;
import static java.time.Month.SEPTEMBER;
import static java.time.Month.JULY;

public class PatrimoineZetyCas implements Supplier<Patrimoine> {

    @Override
    public Patrimoine get() {
        var zety = new Personne("Zety");
        var dateEtude = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel("Ordinateur", dateEtude, 1200000, dateEtude, -0.10);
        var vetements = new Materiel("Vêtements", dateEtude, 1500000, dateEtude, -0.50);
        var argentEspeces = new Argent("Argent en espèces", dateEtude, 800000);
        var compteBancaire = new Argent("Compte bancaire", dateEtude, 100000);

        new FluxArgent("Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200000, 27);
        new FluxArgent("Frais de tenue de compte", compteBancaire, dateEtude, LocalDate.of(2024, SEPTEMBER, 17), -20000, 25);

        var possessions = Set.of(ordinateur, vetements, argentEspeces, compteBancaire);

        return new Patrimoine("Patrimoine de Zety", zety, dateEtude, possessions);
    }
}