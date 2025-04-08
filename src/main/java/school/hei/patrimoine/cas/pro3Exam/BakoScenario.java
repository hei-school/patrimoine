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

import static java.util.Calendar.APRIL;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

public class BakoScenario extends Cas {
    public BakoScenario(LocalDate ajd, LocalDate finSimulation, Map<Personne, Double> possesseurs) {
        super(ajd, finSimulation, possesseurs);
    }

    @Override
    protected Devise devise() {
        return MGA;
    }

    @Override
    protected String nom() {
        return "Scénario financier de Bako";
    }

    @Override
    protected void init() {
    }

    @Override
    protected void suivi() {
    }

    @Override
    public Set<Possession> possessions() {
        var acquisitionDate = LocalDate.of(2025, APRIL, 8);
        var bniAccount = new Compte("Compte courant BNI", acquisitionDate, ariary(2_000_000));
        var bmoiAccount = new Compte("Compte d'épargne BMOI", acquisitionDate, ariary(625_000));
        var coffreFort = new Compte("Coffre-fort", acquisitionDate, ariary(1_750_000));
        var laptop = new Materiel("Laptop", acquisitionDate, acquisitionDate, ariary(3_000_000), -0.12);

        var endDate = LocalDate.of(2025, 12, 31);

        new FluxArgent(
            "Salaire mensuel",
            bniAccount,
            LocalDate.of(2025, APRIL, 2),
            endDate,
            2,
            ariary(2_125_000)
        );

        new FluxArgent(
            "Transfert d'épargne mensuel de la BNI",
            bniAccount,
            LocalDate.of(2025, APRIL, 3),
            endDate,
            3,
            ariary(-200_000)
        );
        new FluxArgent(
            "Transfert d'épargne mensuel vers la BMOI",
            bmoiAccount,
            LocalDate.of(2025, APRIL, 3),
            endDate,
            3,
            ariary(200_000)
        );

        new FluxArgent(
            "Loyer mensuel",
            bniAccount,
            LocalDate.of(2025, APRIL, 26),
            endDate,
            26,
            ariary(-600_000)
        );

        new FluxArgent(
            "Frais de subsistance mensuels",
            bniAccount,
            LocalDate.of(2025, APRIL, 1),
            endDate,
            1,
            ariary(-700_000)
        );

        return Set.of(bniAccount, bmoiAccount, coffreFort, laptop);
    }
} 