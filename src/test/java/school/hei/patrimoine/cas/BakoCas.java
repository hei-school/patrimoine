package school.hei.patrimoine.cas;

import static java.time.Month.DECEMBER;
import static java.time.Month.APRIL;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class BakoCas extends Cas {

    private final Compte compteBNI;
    private final Compte compteBMOI;
    private final Compte coffreFort;
    private final Materiel ordinateur;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Personne personne;

    public BakoCas(int startYear, int endYear, Personne personne, int objectif) {
        super(LocalDate.of(startYear, APRIL, 8), LocalDate.of(endYear, DECEMBER, 31), personne);
        this.startDate = LocalDate.of(startYear, APRIL, 8);
        this.endDate = LocalDate.of(endYear, DECEMBER, 31);
        this.personne = personne;

        // Initialisation des comptes et possessions
        this.compteBNI = new Compte("Compte BNI", LocalDate.now(), ariary(2_000_000));
        this.compteBMOI = new Compte("Compte BMOI", LocalDate.now(), ariary(625_000));
        this.coffreFort = new Compte("Coffre Fort", LocalDate.now(), ariary(1_750_000));
        this.ordinateur = new Materiel("Ordinateur portable", startDate.minusDays(3), startDate, ariary(3_000_000), 0);
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
        new FluxArgent("Virement de salaire", compteBNI, startDate, ariary(2_125_000));
    }

    @Override
    public Set<Possession> possessions() {
        var trainDeVie = new FluxArgent("Vie courante", compteBNI, startDate, endDate, 15, ariary(1_300_000)); // Dépenses mensuelles (600k colocation + 700k autres)
        var ordinateurDepreciation = new Materiel("Ordinateur portable", startDate.minusDays(3), startDate, ariary(3_000_000), 12); // Amortissement de 12% par an
        return Set.of(compteBNI, compteBMOI, coffreFort, ordinateurDepreciation);
    }

    @Override
    protected void suivi() {
        new Correction(new FluxArgent("Correction à la hausse", compteBNI, LocalDate.now(), ariary(0)));
    }

    public Patrimoine projectionFuture(LocalDate dateFuture) {
        // Projection des comptes avec les flux financiers
        Compte compteBNIFutur = compteBNI.projectionFuture(dateFuture);
        new FluxArgent("Virement salaire", compteBNIFutur, startDate, ariary(2_125_000));
        new FluxArgent("Dépenses courantes", compteBNIFutur, startDate, endDate, 15, ariary(1_300_000)); // Dépenses mensuelles

        Compte compteBMOIFutur = compteBMOI.projectionFuture(dateFuture);
        Compte coffreFortFutur = coffreFort.projectionFuture(dateFuture);
        Materiel ordinateurFutur = (Materiel) ordinateur.projectionFuture(dateFuture);

        // Création du patrimoine total de Bako
        return Patrimoine.of(
                personne.nom(),
                devise(),
                dateFuture,
                personne,
                Set.of(compteBNIFutur, compteBMOIFutur, coffreFortFutur, ordinateurFutur)
        );
    }
}

