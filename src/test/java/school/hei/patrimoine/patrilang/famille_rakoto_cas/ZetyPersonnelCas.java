package school.hei.patrimoine.patrilang.famille_rakoto_cas;

import static java.time.Month.FEBRUARY;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.objectif.Objectif;
import school.hei.patrimoine.modele.possession.*;

public class ZetyPersonnelCas extends Cas {
  private final Compte zetyPersonnel;
  private final Compte zetyLoyerMaison;
  private final Creance zetyCreance;
  private final Dette zetyDette;

  public ZetyPersonnelCas(
      LocalDate ajd,
      LocalDate finSimulation,
      Map<Personne, Double> possesseurs,
      Compte zetyPersonnel,
      Compte zetyLoyerMaison,
      Creance zetyCreance,
      Dette zetyDette) {
    super(ajd.plusMonths(1).plusDays(5), finSimulation.minusYears(1), possesseurs);
    this.zetyPersonnel = zetyPersonnel;
    this.zetyLoyerMaison = zetyLoyerMaison;
    this.zetyCreance = zetyCreance;
    this.zetyDette = zetyDette;
  }

  @Override
  protected Devise devise() {
    return MGA;
  }

  @Override
  protected String nom() {
    return "ZetyPersonnel";
  }

  @Override
  protected void init() {
    new Objectif(zetyPersonnel, ajd, ariary(1_000_000));
    new FluxArgent("initComptePersonnel", zetyPersonnel, ajd, ariary(1_000_000));
  }

  @Override
  public Set<Possession> possessions() {
    new GroupePossession(
        "TrainDeVie",
        MGA,
        ajd,
        Set.of(
            new FluxArgent(
                "abonnementWifi", zetyPersonnel, ajd, LocalDate.MAX, 15, ariary(-40_000)),
            new FluxArgent("nourriture", zetyLoyerMaison, ajd, LocalDate.MAX, 1, ariary(-12_000)),
            new FluxArgent("JIRAMA", zetyLoyerMaison, ajd, LocalDate.MAX, 5, ariary(-100_000))));

    new GroupePossession(
        "SalaireMensuel",
        MGA,
        ajd,
        Set.of(
            new FluxArgent(
                "salaireMensuel", zetyPersonnel, ajd, LocalDate.MAX, 31, ariary(500_000))));

    return Set.of(zetyPersonnel, zetyLoyerMaison, zetyCreance, zetyDette);
  }

  @Override
  protected void suivi() {
    var t1 = LocalDate.of(2025, FEBRUARY, 2);
    new Objectif(zetyPersonnel, t1, ariary(2_000_000));
    new Correction(new FluxArgent("correction1", zetyPersonnel, t1, ariary(540_000)));
  }
}
