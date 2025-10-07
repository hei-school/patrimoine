package school.hei.patrimoine.modele.recouppement;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.recouppement.RecoupeurDePossessions.withoutCompteCorrections;

import java.util.HashSet;
import java.util.Set;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Possession;

public record RecoupeurDeCasSet(CasSet planned, CasSet done) {
  public CasSet getRecouped() {
    return new CasSet(
        done.set().stream()
            .map(cas -> casWithRecoupementCorrections(cas, planned))
            .collect(toSet()),
        done.objectifFinal());
  }

  public static RecoupeurDeCasSet of(CasSet planned, CasSet done) {
    return new RecoupeurDeCasSet(planned, done);
  }

  private static Cas casWithRecoupementCorrections(Cas doneCas, CasSet plannedCasSet) {
    var plannedCas =
        plannedCasSet.set().stream()
            .filter(cas -> cas.patrimoine().nom().equals(doneCas.patrimoine().nom()))
            .findFirst()
            .orElseThrow();

    var corrections =
        RecoupeurDePossessions.of(
                doneCas.getFinSimulation(), plannedCas.patrimoine(), doneCas.patrimoine())
            .getCorrections();

    Set<Possession> possessions = new HashSet<>(corrections);
    possessions.addAll(withoutCompteCorrections(doneCas.patrimoine().getPossessions()));

    return new Cas(
        doneCas.getAjd(), doneCas.getFinSimulation(), doneCas.patrimoine().getPossesseurs()) {
      @Override
      protected Devise devise() {
        return doneCas.patrimoine().getDevise();
      }

      @Override
      protected String nom() {
        return doneCas.patrimoine().getNom();
      }

      @Override
      protected void init() {}

      @Override
      protected void suivi() {}

      @Override
      public Set<Possession> possessions() {
        return possessions;
      }
    };
  }
}
