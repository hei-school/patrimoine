package school.hei.patrimoine.cas.pro3Exam;

import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static java.util.Calendar.APRIL;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

public class EtudiantCas extends Cas {
    public EtudiantCas(LocalDate ajd, LocalDate finSimulation, Map<Personne, Double> possesseurs) {
        super(ajd, finSimulation, possesseurs);
    }

    @Override
    protected Devise devise() {
        return MGA;
    }

    @Override
    protected String nom() {
        return "Cas etudiant PRO3";
    }

    @Override
    protected void init() {

    }

    @Override
    protected void suivi() {

    }

    @Override
    public Set<Possession> possessions() {
        var bmoi= new Compte("BMOI", LocalDate.of(2025, APRIL, 1), new Argent(0, MGA));

        var DebutTravail= LocalDate.of(2025, APRIL, 27);
        var finTravail= DebutTravail.plusYears(1);
        new FluxArgent("Salaire", bmoi, DebutTravail, finTravail, 4, ariary(5_000_000));
        new FluxArgent("Depense", bmoi, DebutTravail, finTravail, 27, ariary(-3_700_000));

        return Set.of(bmoi);
    }
}
