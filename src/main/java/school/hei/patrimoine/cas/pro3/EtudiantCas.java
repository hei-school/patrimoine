package school.hei.patrimoine.cas.pro3;

import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.Set;

import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Argent.euro;
import static school.hei.patrimoine.modele.Devise.MGA;

public class EtudiantCas extends Cas {

    public EtudiantCas(
            LocalDate ajd,
            LocalDate finSimulation,
            Map<Personne, Double> possesseurs) {
        super(ajd, finSimulation, possesseurs);
    }

    @Override
    protected Devise devise() {
        return MGA;
    }

    @Override
    protected String nom() {
        return "Cas d'un étudiant";
    }

    @Override
    protected void init() {

    }

    @Override
    protected void suivi() {

    }

    @Override
    public Set<Possession> possessions() {
        var bmoi = new Compte("BMOI", LocalDate.of(2025, Month.MARCH, 1),new Argent(0,MGA));

        var debutTravail = LocalDate.of(2025,Month.MARCH,26);
        var finTravail = debutTravail.plusYears(1);

        new FluxArgent("Salaire", bmoi, debutTravail,finTravail,4,ariary(4_000_000));
        new FluxArgent("Depense", bmoi, debutTravail,finTravail,26,ariary(-3_500_000));

        return Set.of(bmoi);
    }
}