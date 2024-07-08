package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;

public class ZetyEtudieEn2023 implements Supplier<Patrimoine> {
    private final LocalDate ajd = LocalDate.of(2024, 7, 3);

    private final LocalDate novembre2023 = LocalDate.of(2023, 11, 1);
    private final LocalDate août2024 = LocalDate.of(2024, 8, 31);

    private final LocalDate debutDette = LocalDate.of(2024, 9, 18);

    private final LocalDate paiementFraisScolarite = LocalDate.of(2024, 9, 21);
    private final LocalDate debutTrainDeVie = LocalDate.of(2024, 10, 1);
    private final LocalDate finTrainDeVie = LocalDate.of(2025, 2, 13);

    private final LocalDate departAllemagne = LocalDate.of(2025, 2, 14);
    private final LocalDate arriveeAllemagne = LocalDate.of(2025, 2, 15);

    private Set<Possession> possessionsZety(Argent compteBancaire) {
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
        var fraisScolaire2023 = new FluxArgent(
                "Frais de scolarité 2023-2024",
                argent,
                novembre2023,
                août2024,
                -200_000,
                27
        );
        var tenueCompteBancaire = new FluxArgent(
                "Tenue de compte",
                compteBancaire,
                novembre2023,
                LocalDate.MAX,
                -20_000,
                25
        );
        var dette = new Dette("Dette bancaire", debutDette, -11_000_000);
        var emprunt = new FluxArgent(
                "Prêt bancaire",
                compteBancaire,
                debutDette,
                debutDette,
                10_000_000,
                debutDette.getDayOfMonth()
        );
        var fraisScolaire2024 = new FluxArgent(
                "Frais de scolarité 2024-2025",
                compteBancaire,
                paiementFraisScolarite,
                paiementFraisScolarite,
                -2_500_000,
                paiementFraisScolarite.getDayOfMonth()
        );

        var donParents = new FluxArgent(
                "Don des parents",
                argent,
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2025, 12, 31),
                100_000,
                15
        );

        var trainDeVie = new FluxArgent(
                "Train de vie",
                argent,
                debutTrainDeVie,
                finTrainDeVie,
                -250_000,
                1
        );

        var compteBancaireAllemagne = new Argent("Compte Deutsche Bank", arriveeAllemagne, 0);
        var detteAllemagne = new Dette("Dette Allemagne", arriveeAllemagne, -7000);

        return Set.of(
                ordinateur,
                vetements,
                argent,
                fraisScolaire2023,
                compteBancaire,
                tenueCompteBancaire,
                emprunt, dette,
                fraisScolaire2024,
                donParents,
                trainDeVie,
                compteBancaireAllemagne,
                detteAllemagne
        );
    }

    @Override
    public Patrimoine get() {
        var zety = new Personne("Zety");
        var compteBancaire = new Argent(
                "Compte bancaire", ajd.minusDays(1), 100_000);
        return new Patrimoine(
                "Zety",
                zety,
                ajd,
                possessionsZety(compteBancaire)
        );
    }
}
