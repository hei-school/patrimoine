package school.hei.patrimoine.cas;

import static java.time.Month.FEBRUARY;
import static java.time.Month.MAY;
import static java.time.Month.SEPTEMBER;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class EtudiantPireCas extends Cas {

  private final Argent financeur;

  public EtudiantPireCas() {
    super(LocalDate.of(2024, SEPTEMBER, 17), LocalDate.MAX, Set.of(new Personne("Ilo")));
    financeur = new Argent("Espèces", LocalDate.MIN, 0);
  }

  @Override
  protected String nom() {
    return "Ilo";
  }

  @Override
  protected void init() {
    new FluxArgent("Init compte Espèces", financeur, LocalDate.of(2024, FEBRUARY, 1), 700_000);
  }

  @Override
  protected Supplier<Set<Possession>> possessionsSupplier() {
    return () -> {
      var au13mai24 = LocalDate.of(2024, MAY, 13);
      var trainDeVie =
          new FluxArgent(
              "Vie courante",
              financeur,
              au13mai24.minusDays(100),
              au13mai24.plusDays(100),
              -100_000,
              15);
      var mac = new Materiel("MacBook Pro", au13mai24, 500_000, au13mai24.minusDays(3), -0.9);
      return Set.of(financeur, trainDeVie, mac);
    };
  }

  @Override
  protected void suivi() {
    new Correction(new FluxArgent("Correction à la hausse", financeur, ajd, 50_000));
  }
}
