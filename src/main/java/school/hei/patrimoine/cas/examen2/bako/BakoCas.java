package school.hei.patrimoine.cas.examen2.bako;

import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.TransfertArgent;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static school.hei.patrimoine.modele.Argent.ariary;

public class BakoCas extends Cas {

    public BakoCas(
            LocalDate ajd,
            LocalDate finSimulation,
            Map<Personne, Double> possesseurs) {
        super(ajd, finSimulation, possesseurs);
    }

    @Override
    protected Devise devise() {
        return Devise.MGA;
    }

    @Override
    protected String nom() {
        return "Bako's Case";
    }

    @Override
    protected void init() {

    }

    @Override
    protected void suivi() {

    }

    @Override
    public Set<Possession> possessions() {
        var ajd = getAjd();
        var bni = new Compte("BNI", ajd, ariary(2_000_000));
        var bmoi = new Compte("BMOI", ajd, ariary(625_000));
        var coffre = new Compte("Home Safe", ajd, ariary(1_750_000));
        // Salary of Bako
        new FluxArgent(
                "Salary",
                bni,
                LocalDate.of(2025, 4, 2),
                LocalDate.of(2025, 12, 2),
                1,
                ariary(2_125_000));

        // Automatic transfer for savings to BMOI account every 3rd of the month
        new TransfertArgent(
                "Savings transfer",
                bni,
                bmoi,
                LocalDate.of(2025, 4, 3), // Start date
                LocalDate.of(2025, 12, 3), // End date
                1,
                ariary(200_000));

        // Monthly expenses for shared housing and other costs
        new FluxArgent(
                "Shared housing",
                bni,
                LocalDate.of(2025, 4, 26), // Payment start
                LocalDate.of(2025, 12, 26), // End
                1,
                ariary(-600_000));

        new FluxArgent(
                "Living expenses",
                bni,
                LocalDate.of(2025, 4, 1), // Start date
                LocalDate.of(2025, 12, 1), // End date
                1,
                ariary(-700_000));

        // Modeling of the laptop with 12% annual depreciation
        var ordinateur = new Materiel(
                "Laptop",
                ajd,
                ajd,
                ariary(3_000_000),
                -0.12);

        return Set.of(bni, bmoi, coffre, ordinateur);
    }
}
