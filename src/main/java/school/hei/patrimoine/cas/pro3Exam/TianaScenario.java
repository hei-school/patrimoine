package school.hei.patrimoine.cas.pro3Exam;

import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

public class TianaScenario extends Cas {
    public TianaScenario(LocalDate ajd, LocalDate finSimulation, Map<Personne, Double> possesseurs) {
        super(ajd, finSimulation, possesseurs);
    }

    @Override
    protected Devise devise() {
        return MGA;
    }

    @Override
    protected String nom() {
        return "Scénario financier de Tiana";
    }

    @Override
    protected void init() {
    }

    @Override
    protected void suivi() {
    }

    @Override
    public Set<Possession> possessions() {
        var acquisitionDate = LocalDate.of(2025, 4, 8);
        var bankAccount = new Compte("Compte bancaire", acquisitionDate, ariary(60_000_000));
        var plot = new Materiel("Terrain bâti", acquisitionDate, acquisitionDate, ariary(100_000_000), 0.10);

        var endDate = LocalDate.of(2026, 3, 31);

        // Monthly family expenses
        new FluxArgent(
            "Dépenses familiales mensuelles",
            bankAccount,
            LocalDate.of(2025, 4, 1),
            endDate,
            1,
            ariary(-4_000_000)
        );

        // Project initial payment
        new FluxArgent(
            "Paiement initial du projet",
            bankAccount,
            LocalDate.of(2025, 5, 1),
            LocalDate.of(2025, 5, 1),
            1,
            ariary(7_000_000)
        );

        // Monthly project expenses
        new FluxArgent(
            "Dépenses mensuelles du projet",
            bankAccount,
            LocalDate.of(2025, 6, 5),
            LocalDate.of(2025, 12, 5),
            5,
            ariary(-5_000_000)
        );

        // Project final payment
        new FluxArgent(
            "Paiement final du projet",
            bankAccount,
            LocalDate.of(2026, 1, 31),
            LocalDate.of(2026, 1, 31),
            31,
            ariary(63_000_000)
        );

        // Bank loan
        new FluxArgent(
            "Prêt bancaire",
            bankAccount,
            LocalDate.of(2025, 7, 27),
            LocalDate.of(2025, 7, 27),
            27,
            ariary(20_000_000)
        );

        // Monthly loan repayments
        new FluxArgent(
            "Remboursement mensuel du prêt",
            bankAccount,
            LocalDate.of(2025, 8, 27),
            LocalDate.of(2026, 7, 27),
            27,
            ariary(-2_000_000)
        );

        return Set.of(bankAccount, plot);
    }
} 