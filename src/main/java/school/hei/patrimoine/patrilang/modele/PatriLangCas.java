package school.hei.patrimoine.patrilang.modele;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Possession;

public final class PatriLangCas extends Cas {
  private final Devise devise;
  private final String nom;
  private final Runnable init;
  private final Runnable suivi;
  private final Supplier<Set<Possession>> possessionsSupplier;

  public PatriLangCas(
      String nom,
      Devise devise,
      LocalDate ajd,
      LocalDate finSimulation,
      Map<Personne, Double> possesseurs,
      Runnable init,
      Runnable suivi,
      Supplier<Set<Possession>> possessionsSupplier) {
    super(ajd, finSimulation, possesseurs);

    this.devise = devise;
    this.nom = nom;
    this.init = init;
    this.suivi = suivi;
    this.possessionsSupplier = possessionsSupplier;
  }

  @Override
  protected Devise devise() {
    return devise;
  }

  @Override
  protected String nom() {
    return nom;
  }

  @Override
  protected void init() {
    init.run();
  }

  @Override
  protected void suivi() {
    suivi.run();
  }

  @Override
  public Set<Possession> possessions() {
    return possessionsSupplier.get();
  }

  public void validate() {
    this.init();
    this.suivi();
    this.possessions();
  }
}
