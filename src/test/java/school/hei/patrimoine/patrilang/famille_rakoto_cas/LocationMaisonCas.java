package school.hei.patrimoine.patrilang.famille_rakoto_cas;

import static java.time.Month.DECEMBER;
import static java.time.Month.MARCH;
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

public class LocationMaisonCas extends Cas {
  private final Compte zetyLoyerMaison;
  private final Compte loyerMaison;
  private final Compte litaPersonnel;
  private final Compte rasoaPersonnel;
  private final Compte newCompte;
  private static final LocalDate AU_02_MARS_2025 = LocalDate.of(2025, MARCH, 2);

  public LocationMaisonCas(
      LocalDate ajd,
      LocalDate finSimulation,
      Map<Personne, Double> possesseurs,
      Compte loyerMaison,
      Compte zetyLoyerMaison,
      Compte rasoaPersonnel,
      Compte litaPersonnel) {
    super(ajd, finSimulation.minusYears(1).minusMonths(2).minusDays(10), possesseurs);
    this.zetyLoyerMaison = zetyLoyerMaison;
    this.loyerMaison = loyerMaison;
    this.litaPersonnel = litaPersonnel;
    this.rasoaPersonnel = rasoaPersonnel;
    this.newCompte = new Compte("newCompte", AU_02_MARS_2025, ariary(500_000));
  }

  @Override
  protected Devise devise() {
    return MGA;
  }

  @Override
  protected String nom() {
    return "LocationMaison";
  }

  @Override
  protected void init() {
    new Objectif(loyerMaison, ajd, ariary(500_000));
    new FluxArgent("initCompteLoyerMaison", loyerMaison, ajd, ariary(500_000));
  }

  @Override
  protected void suivi() {
    new Correction(
        new FluxArgent("correctionOuvertureCompte", newCompte, AU_02_MARS_2025, ariary(-500_000)));
  }

  @Override
  public Set<Possession> possessions() {
    var au31Décembre2025 = LocalDate.of(2025, DECEMBER, 31);
    new GroupePossession(
        "Rem2025",
        MGA,
        ajd,
        Set.of(
            new FluxArgent(
                "remZety2025", zetyLoyerMaison, ajd, au31Décembre2025, 1, ariary(400_000)),
            new FluxArgent(
                "remRasoa2025", rasoaPersonnel, ajd, au31Décembre2025, 1, ariary(500_000)),
            new FluxArgent(
                "remLita2025", litaPersonnel, ajd, au31Décembre2025, 1, ariary(100_000))));

    new GroupePossession(
        "RevenusLoyer",
        MGA,
        ajd,
        Set.of(
            new FluxArgent(
                "receptionLoyer", loyerMaison, ajd, LocalDate.MAX, 29, ariary(1_000_000))));

    new GroupePossession(
        "ChargesLoyer",
        MGA,
        ajd,
        Set.of(
            new FluxArgent("paiementCommune", loyerMaison, ajd, LocalDate.MAX, 1, ariary(-200_000)),
            new FluxArgent("JIRAMA", loyerMaison, ajd, LocalDate.MAX, 1, ariary(-100_000))));

    return Set.of(newCompte, loyerMaison);
  }
}
