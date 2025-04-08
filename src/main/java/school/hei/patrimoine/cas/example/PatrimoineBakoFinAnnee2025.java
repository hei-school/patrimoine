package school.hei.patrimoine.cas.example;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import java.util.function.Supplier;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

public class PatrimoineBakoFinAnnee2025 implements Supplier<Patrimoine> {

    public static final LocalDate DATE_ACQUISITION = LocalDate.of(2025, Month.APRIL, 8);
    public static final LocalDate FIN_PROJECTION = LocalDate.of(2025, Month.DECEMBER, 31);
    public static final LocalDate EVALUATION_ORDINATEUR = DATE_ACQUISITION.plusYears(1);

    private Compte compteBNI() {
        return new Compte("Compte BNI", DATE_ACQUISITION, ariary(2_000_000));
    }

    private Compte compteBMOI() {
        return new Compte("Compte BMOI", DATE_ACQUISITION, ariary(625_000));
    }

    private Compte coffreFort() {
        return new Compte("Coffre Fort", DATE_ACQUISITION, ariary(1_750_000));
    }

    private Materiel ordinateur() {
        return new Materiel("Ordinateur Portable", DATE_ACQUISITION, EVALUATION_ORDINATEUR, ariary(3_000_000), -0.12);
    }

    private void ajouterFluxCompteBNI(Compte compteBNI) {
        compteBNI.addFinancés(new FluxArgent("Salaire mensuel", compteBNI, DATE_ACQUISITION.withDayOfMonth(2), FIN_PROJECTION, 9, ariary(2_125_000)));
        compteBNI.addFinancés(new FluxArgent("Loyer", compteBNI, DATE_ACQUISITION.withDayOfMonth(26), FIN_PROJECTION, 9, ariary(-600_000)));
        compteBNI.addFinancés(new FluxArgent("Dépenses de vie", compteBNI, DATE_ACQUISITION.withDayOfMonth(1), FIN_PROJECTION, 9, ariary(-700_000)));
    }

    private void ajouterFluxCompteBMOI(Compte compteBMOI) {
        compteBMOI.addFinancés(new FluxArgent("Transfert épargne", compteBMOI, DATE_ACQUISITION.withDayOfMonth(3), FIN_PROJECTION, 9, ariary(200_000)));
    }


    @Override
    public Patrimoine get() {
        Personne bako = new Personne("Bako");
        Compte compteBNI = compteBNI();
        Compte compteBMOI = compteBMOI();
        Compte coffreFort = coffreFort();
        Materiel ordinateur = ordinateur();

        ajouterFluxCompteBNI(compteBNI);
        ajouterFluxCompteBMOI(compteBMOI);

        Set<Possession> possessions = Set.of(compteBNI, compteBMOI, coffreFort, ordinateur);

        return Patrimoine.of("Bako au 8 avril 2025", MGA, DATE_ACQUISITION, bako, possessions)
                .projectionFuture(FIN_PROJECTION);
    }
}
