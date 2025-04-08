package school.hei.patrimoine.cas.example;

import static java.time.Month.APRIL;
import static java.time.Month.DECEMBER;
import static java.time.Month.MAY;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;

import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

public class BakoCas extends Cas {


    private final Compte bni = new Compte("BNI", LocalDate.of(2025, APRIL, 8), ariary(2_000_000));
    private final Compte bmoi = new Compte("BMOI", LocalDate.of(2025, APRIL, 8), ariary(625_000));
    private final Compte coffre = new Compte("Coffre", LocalDate.of(2025, APRIL, 8), ariary(1_750_000));

    public BakoCas() {
        super(LocalDate.of(2025, APRIL, 8), LocalDate.of(2025, DECEMBER, 31), new Personne("Bako"));
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
        // Initialisé via les comptes déjà
    }

    @Override
    public Set<Possession> possessions() {
        var salaire = new FluxArgent("Salaire", bni,
                LocalDate.of(2025, MAY, 2), LocalDate.of(2025, DECEMBER, 2),
                1, ariary(2_125_000));

        var virementEpargne = new TransfertArgent("Virement vers BMOI", bni, bmoi,
                LocalDate.of(2025, MAY, 3), LocalDate.of(2025, DECEMBER, 3),
                1, ariary(200_000));

        var depensesVie = new FluxArgent("Dépenses mensuelles", bni,
                LocalDate.of(2025, MAY, 1), LocalDate.of(2025, DECEMBER, 1),
                1, ariary(-700_000));

        var loyer = new FluxArgent("Loyer", bni,
                LocalDate.of(2025, MAY, 26), LocalDate.of(2025, DECEMBER, 26),
                1, ariary(-600_000));

        var ordinateur = new Materiel("Ordinateur portable", LocalDate.of(2025, APRIL, 8),
                LocalDate.of(2025, DECEMBER, 31),
                ariary(3_000_000), -0.12);

        return Set.of(bni, bmoi, coffre, salaire, virementEpargne, depensesVie, loyer, ordinateur);
    }

    @Override
    protected void suivi() {
        // Pas de corrections spécifiques à faire ici
    }
}
