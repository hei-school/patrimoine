package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;

public class ZetyEtudieEn2023 implements Supplier<Patrimoine> {
    private final LocalDate ajd = LocalDate.of(2024, 7, 3);
    private final LocalDate novembre2023 = LocalDate.of(2023, 11, 1);
    private final LocalDate août2024 = LocalDate.of(2024, 8, 31);

    private Set<Possession> possessionsZety() {
        var ordinateur = new Materiel(
                "Ordinateur",
                ajd,
                1_200_000,
                ajd.minusDays(1),
                -0.1
        );
        var vetements = new Materiel(
                "Vêtements",
                ajd,
                1_500_000,
                ajd.minusDays(1),
                -0.5
        );
        var argent = new Argent("Espèces", ajd, 800_000);
        var fraisScolaire = new FluxArgent(
                "Frais de scolarité",
                argent,
                novembre2023,
                août2024,
                -200_000,
                27
        );
        var compteBancaire = new Argent(
                "Compte bancaire", ajd.minusDays(1), 100_000);
        var tenueCompteBancaire = new FluxArgent(
                "Tenue de compte",
                compteBancaire,
                novembre2023,
                LocalDate.MAX,
                -20_000,
                25
        );
        return Set.of(ordinateur, vetements, argent, fraisScolaire, compteBancaire, tenueCompteBancaire);
    }

    @Override
    public Patrimoine get() {
        var zety = new Personne("Zety");
        return new Patrimoine(
                "Zety",
                zety,
                ajd,
                possessionsZety()
        );
    }
}
