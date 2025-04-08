package school.hei.patrimoine.cas;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class TianaCas extends Cas {

    private final Compte compteBancaire;
    private final Materiel terrain;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Personne personne;

    public TianaCas(int startYear, int endYear, Personne personne) {
        super(LocalDate.of(startYear, Month.APRIL, 8), LocalDate.of(endYear, Month.MARCH, 31), personne);
        this.startDate = LocalDate.of(startYear, Month.APRIL, 8);
        this.endDate = LocalDate.of(endYear, Month.MARCH, 31);
        this.personne = personne;

        this.compteBancaire = new Compte("Compte Bancaire", LocalDate.now(), Argent.ariary(60_000_000));
        this.terrain = new Materiel("Terrain bâti", startDate.minusDays(3), startDate, Argent.ariary(100_000_000), 10);  // Appreciation de 10% par an
    }

    @Override
    protected String nom() {
        return personne.nom();
    }

    @Override
    protected Devise devise() {
        return Devise.MGA;
    }

    @Override
    protected void init() {
        // Revenus du projet entrepreneurial
        new FluxArgent("Revenus brut projet", compteBancaire, LocalDate.of(2025, 5, 1), Argent.ariary(7_000_000)); // 10% du montant brut (1 mai 2025)
    }

    @Override
    public Set<Possession> possessions() {
        // Flux mensuels et autres possessions
        var projetDepenses = new FluxArgent("Dépenses projet", compteBancaire, LocalDate.of(2025, 6, 1), LocalDate.of(2025, 12, 31), 6, Argent.ariary(5_000_000)); // Dépenses mensuelles pour le projet
        var depensesMensuelles = new FluxArgent("Dépenses familiales", compteBancaire, startDate, endDate, 12, Argent.ariary(4_000_000)); // Dépenses familiales mensuelles
        var remboursementPret = new FluxArgent("Remboursement prêt", compteBancaire, LocalDate.of(2025, 8, 27), LocalDate.of(2026, 7, 27), 12, Argent.ariary(2_000_000)); // Remboursement mensuel du prêt

        return Set.of(compteBancaire, terrain, projetDepenses, depensesMensuelles, remboursementPret);
    }

    @Override
    protected void suivi() {
        // Finalisation du projet
        new FluxArgent("Revenus brut projet", compteBancaire, LocalDate.of(2026, 1, 31), Argent.ariary(63_000_000)); // 90% du montant brut (31 janvier 2026)
    }

    public Patrimoine projectionFuture(LocalDate dateFuture) {

        Compte compteBancaireFutur = compteBancaire.projectionFuture(dateFuture);
        Materiel terrainFutur = (Materiel) terrain.projectionFuture(dateFuture);

        return Patrimoine.of(
                personne.nom(),
                devise(),
                dateFuture,
                personne,
                Set.of(compteBancaireFutur, terrainFutur)
        );
    }
}
