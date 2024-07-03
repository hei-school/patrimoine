package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;

public class PatrimoineEtudiantZetyCas implements Supplier<Patrimoine> {

    private final LocalDate dateDebut = LocalDate.of(2024, 7, 3);
    private final LocalDate dateFin = LocalDate.of(2024, 9, 17);

    @Override
    public Patrimoine get() {
        var zety = new Personne("Zety");
        
        var ordinateur = new Materiel("Ordinateur", dateDebut, 1_200_000, dateDebut, -0.1);
        var vetements = new Materiel("Vêtements", dateDebut, 1_500_000, dateDebut, -0.5);
        var argent = new Argent("Argent liquide", dateDebut, 800_000);
        var compteBancaire = new Argent("Compte bancaire", dateDebut, 100_000);

        new FluxArgent("Frais de scolarité", argent, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);

        new FluxArgent("Frais bancaires", compteBancaire, dateDebut, dateFin.plusYears(10), -20_000, 25);

        Set<Possession> possessions = Set.of(ordinateur, vetements, argent, compteBancaire);

        return new Patrimoine("Zety étudie en 2023-2024", zety, dateDebut, possessions);
    }
}
