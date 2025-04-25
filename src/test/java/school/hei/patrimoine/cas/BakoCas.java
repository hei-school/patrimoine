package school.hei.patrimoine.cas;

import static java.time.Month.APRIL;
import static java.time.Month.DECEMBER;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;

import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

public class BakoCas extends Cas {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Personne bako;

    private final Compte bni;
    private final Compte bmoi;
    private final Compte coffre;

    public BakoCas() {
        super(LocalDate.of(2025, APRIL, 8), LocalDate.of(2025, DECEMBER, 31), new Personne("Bako"));
        this.startDate = LocalDate.of(2025, APRIL, 8);
        this.endDate = LocalDate.of(2025, DECEMBER, 31);
        this.bako = new Personne("Bako");

        this.bni = new Compte("BNI", startDate, ariary(2_000_000));
        this.bmoi = new Compte("BMOI", startDate, ariary(625_000));
        this.coffre = new Compte("Coffre", startDate, ariary(1_750_000));
    }

    @Override
    protected String nom() {
        return "BakoCas";
    }

    @Override
    protected Devise devise() {
        return MGA;
    }

    @Override
    protected void init() {

    }

    @Override
    public Set<Possession> possessions() {
        var salaire = new FluxArgent(
                "Salaire BNI",
                bni,
                LocalDate.of(2025, 4, 2),
                endDate,
                1,
                ariary(2_125_000)
        );

        var epargneAuto = new FluxArgent(
                "Épargne automatique vers BMOI",
                bmoi,
                LocalDate.of(2025, 4, 3),
                endDate,
                1,
                ariary(200_000)
        );

        var depensesVie = new FluxArgent(
                "Dépenses de vie",
                bni,
                LocalDate.of(2025, 5, 1),
                endDate,
                1,
                ariary(-700_000)
        );

        var loyer = new FluxArgent(
                "Loyer coloc",
                bni,
                LocalDate.of(2025, 4, 26),
                endDate,
                1,
                ariary(-600_000)
        );

        var ordinateur = new Materiel(
                "Ordinateur portable",
                startDate,
                startDate,
                ariary(3_000_000),
                12
        );

        return Set.of(bni, bmoi, coffre, salaire, epargneAuto, depensesVie, loyer, ordinateur);
    }

    @Override
    protected void suivi() {

    }
}

