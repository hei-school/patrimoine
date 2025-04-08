package school.hei.patrimoine.cas.example;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.Calendar.APRIL;
import static java.util.Calendar.JANUARY;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

public class TianaCas implements Supplier<Patrimoine> {
    public static final LocalDate AU_8_AVRIL_2025 = LocalDate.of(2025, APRIL, 8);

    private Compte compteBancaire() {
        return new Compte("compte bancaire Tiana", AU_8_AVRIL_2025, ariary(60_000_000));
    }

    private Materiel terrainBati() {
        return new Materiel("terrain bâti", AU_8_AVRIL_2025, AU_8_AVRIL_2025, ariary(100_000_000), 0.10);
    }

    private static void addFlux(Compte compte) {
        new FluxArgent("dépenses familiales", compte, AU_8_AVRIL_2025, LocalDate.MAX, 1, ariary(-4_000_000));

        new FluxArgent("dépenses projet", compte, LocalDate.of(2025, 6, 1), LocalDate.of(2025, 12, 31), 5, ariary(-5_000_000));
        new FluxArgent("revenu projet - avance", compte, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 1), 1, ariary(7_000_000));
        new FluxArgent("revenu projet - solde", compte, LocalDate.of(2026, 1, 31), LocalDate.of(2026, 1, 31), 31, ariary(63_000_000));

        new FluxArgent("prêt bancaire", compte, LocalDate.of(2025, 7, 27), LocalDate.of(2025, 7, 27), 27, ariary(20_000_000));
        new FluxArgent("remboursement prêt", compte, LocalDate.of(2025, 8, 27), LocalDate.MAX, 27, ariary(-2_000_000));
    }

    @Override
    public Patrimoine get() {
        var tiana = new Personne("Tiana");
        var compte = compteBancaire();
        addFlux(compte);

        var terrain = terrainBati();

        var groupeComptes = new GroupePossession(
                "compte bancaire",
                MGA,
                AU_8_AVRIL_2025,
                Set.of(compte)
        );

        var groupeActifs = new GroupePossession(
                "actifs immobiliers",
                MGA,
                AU_8_AVRIL_2025,
                Set.of(terrain)
        );

        return Patrimoine.of("Tiana au 8 avril 2025", MGA, AU_8_AVRIL_2025, tiana, Set.of(groupeComptes, groupeActifs));
    }

    public Patrimoine projectionAu31Mars2026() {
        return get().projectionFuture(LocalDate.of(2026, JANUARY, 1).withMonth(3).withDayOfMonth(31));
    }
}