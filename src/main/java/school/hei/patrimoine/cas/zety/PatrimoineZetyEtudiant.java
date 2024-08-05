package school.hei.patrimoine.cas.zety;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.SequencedCollection;
import java.util.Set;

import static java.time.Month.*;

public class PatrimoineZetyEtudiant {

    public Patrimoine get() {
        var zety = new Personne("zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au27noveembre23 = LocalDate.of(2023 , NOVEMBER , 27);
        var au27Aout2024 = LocalDate.of(24 , AUGUST , 27);
        var espèces = new Argent("Espèces", au3juillet24, 800_000);
        var compteBancaire = new Argent("compte" , au3juillet24 , 100_000 );

        var fraisCompteBancaire = new FluxArgent(
                "frais compte bancaire",
                compteBancaire,
                au3juillet24,
                au3juillet24.plusYears(1),
                -20_000,
                25
                );

        var fraisDeScolarite = new FluxArgent(
                "frais de scolarite",
                espèces ,
                au27noveembre23,
                au27Aout2024,
                -200_000,
                27
                );

        var ordinateur = new Materiel(
                "ordinateur",
                au3juillet24,
                1_200_000,
                au3juillet24,
                -0.1);

        var vetement = new Materiel(
                "vetements",
                au3juillet24,
                1_500_000,
                au3juillet24.minusDays(0),
                -0.5

        );

        var dette = new Dette(
                "dette pour frais de scolarite 2024-2025",
                LocalDate.of(2024 , SEPTEMBER , 18),
                -10_000_000
        );

        var remboursement = new FluxArgent(
                "remboursement",
                compteBancaire ,
                LocalDate.of(2024 , SEPTEMBER , 18),
                LocalDate.of(2025 , SEPTEMBER , 18),
                11_000_000 / 365,
                18
        );



        return new Patrimoine(
                "zety etduiant",
                zety,
                au3juillet24,
                Set.of( espèces,
                        fraisDeScolarite,
                        fraisCompteBancaire,
                        compteBancaire,
                        ordinateur ,
                        vetement,
                        dette,
                        remboursement
                        ));
    }
}
