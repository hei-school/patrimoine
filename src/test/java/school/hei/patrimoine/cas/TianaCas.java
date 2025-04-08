package school.hei.patrimoine.cas;

import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;
import java.time.LocalDate;
import java.util.Set;

import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class TianaCas extends Cas {

    private final Compte compteBancaire;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Personne personne;
    private final Compte projetCompte;
    private final Compte pretCompte;

    public TianaCas() {
        super(LocalDate.of(2025, 4, 8), LocalDate.of(2026, 3, 31), new Personne("Tiana"));
        this.startDate = LocalDate.of(2025, 4, 8);
        this.endDate = LocalDate.of(2026, 3, 31);
        this.personne = new Personne("Tiana");
        this.compteBancaire = new Compte("Compte bancaire", LocalDate.now(), ariary(60_000_000));
        this.projetCompte = new Compte("Projet entrepreneurial", LocalDate.now(), ariary(0));
        this.pretCompte = new Compte("Prêt bancaire", LocalDate.now(), ariary(0));
    }

    @Override
    protected String nom() {
        return personne.nom();
    }

    @Override
    protected Devise devise() {
        return MGA;
    }

    @Override
    protected void init() {
        // Flux initial pour le terrain et autres possessions
        new FluxArgent("Initial", compteBancaire, startDate, ariary(60_000_000));
    }

    @Override
    public Set<Possession> possessions() {
        var terrain = new Materiel("Terrain bâti", startDate, startDate.plusYears(1), ariary(100_000_000), 10);

        // Flux des dépenses
        var depensesMensuelles = new FluxArgent("Dépenses familiales", compteBancaire, startDate, endDate, 12, ariary(-4_000_000));

        // Flux du projet entrepreneurial
        var depensesProjet = new FluxArgent("Dépenses projet", compteBancaire, LocalDate.of(2025, 6, 1), LocalDate.of(2025, 12, 31), 6, ariary(-5_000_000));

        // Encaissements du projet (10% en mai et 90% en janvier)
        new FluxArgent("Encaissement projet", projetCompte, LocalDate.of(2025, 5, 1), ariary(7_000_000));
        new FluxArgent("Encaissement projet", projetCompte, LocalDate.of(2026, 1, 31), ariary(63_000_000));

        // Prêt bancaire et remboursement
        new FluxArgent("Prêt bancaire", pretCompte, LocalDate.of(2025, 7, 27), ariary(20_000_000));
        new FluxArgent("Remboursement prêt", compteBancaire, LocalDate.of(2025, 8, 27), ariary(-2_000_000));

        return Set.of(compteBancaire, terrain, depensesMensuelles, depensesProjet, projetCompte, pretCompte);
    }

    @Override
    protected void suivi() {
        new Correction(new FluxArgent("Suivi", compteBancaire, LocalDate.now(), ariary(0)));
    }
}
