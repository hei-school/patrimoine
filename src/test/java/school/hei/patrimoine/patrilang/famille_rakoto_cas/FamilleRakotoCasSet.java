package school.hei.patrimoine.patrilang.famille_rakoto_cas;

import static java.time.Month.APRIL;
import static java.time.Month.JANUARY;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.cas.CasSetSupplier;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;

public class FamilleRakotoCasSet extends CasSetSupplier {
  private static final Argent OBJECTIF_FINAL = ariary(4_884_000);
  private static final LocalDate AJD = LocalDate.of(2025, JANUARY, 10);
  private static final LocalDate FIN_SIMULATION = LocalDate.of(2025, APRIL, 10);

  @Override
  public CasSet get() {
    var zety = new Personne("Zety");
    var lita = new Personne("Lita");
    var rasoa = new Personne("Rasoa");

    var loyerMaison = new Compte("loyerMaison", AJD, ariary(0));
    var zetyPersonnel = new Compte("zetyPersonnel", AJD, ariary(0));
    var zetyLoyerMaison = new Compte("zetyLoyerMaison", AJD, ariary(0));
    var litaPersonnel = new Compte("litaPersonnel", AJD, ariary(0));
    var rasoaPersonnel = new Compte("rasoaPersonnel", AJD, ariary(0));

    var zetyDette = new Dette("zetyDette", AJD, ariary(0));
    var zetyCreance = new Creance("zetyCreance", AJD, ariary(0));

    Map<Personne, Double> zetyPersonnelPossesseur = Map.of(zety, 1.0d);
    Map<Personne, Double> locationMaisonPossesseur =
        Map.of(
            zety, 0.4d,
            rasoa, 0.5d,
            lita, 0.1d);

    var zetyPersonnelCas =
        new ZetyPersonnelCas(
            AJD,
            FIN_SIMULATION,
            zetyPersonnelPossesseur,
            zetyPersonnel,
            zetyLoyerMaison,
            zetyCreance,
            zetyDette);
    var locationMaisonCas =
        new LocationMaisonCas(
            AJD,
            FIN_SIMULATION,
            locationMaisonPossesseur,
            loyerMaison,
            zetyLoyerMaison,
            rasoaPersonnel,
            litaPersonnel);
    return new CasSet(Set.of(zetyPersonnelCas, locationMaisonCas), OBJECTIF_FINAL);
  }
}
