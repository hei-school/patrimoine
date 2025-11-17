package school.hei.patrimoine.modele.recouppement;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.recouppement.CompteGetterFactory.getComptes;
import static school.hei.patrimoine.modele.recouppement.RecoupeurDePossessions.withoutCompteCorrections;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;

@Slf4j
@RequiredArgsConstructor
public class RecoupeurDeCasSet {
  private final CasSet planned;
  private final CasSet done;
  private Set<Compte> casSetComptes;

  public static RecoupeurDeCasSet of(CasSet planned, CasSet done) {
    return new RecoupeurDeCasSet(planned, done);
  }

  public CasSet getRecouped() {
    return new CasSet(
        done.set().stream()
            .map(cas -> withRecoupementCorrections(cas, done, planned))
            .collect(toSet()),
        done.objectifFinal());
  }

  private Set<Compte> getCasSetComptes() {
    if (casSetComptes == null) {
      casSetComptes = getComptes(done);
    }

    return casSetComptes;
  }

  private Cas withRecoupementCorrections(Cas doneCas, CasSet doneCasSet, CasSet plannedCasSet) {
    var plannedCas =
        plannedCasSet.set().stream()
            .filter(cas -> cas.patrimoine().nom().equals(doneCas.patrimoine().nom()))
            .findFirst()
            .orElseThrow();

    var possessions = withoutCompteCorrections(doneCas.patrimoine().getPossessions());

    var corrections =
        RecoupeurDePossessions.of(
                doneCas.getFinSimulation(),
                plannedCas.patrimoine(),
                doneCas.patrimoine(),
                CompteGetterFactory.make(doneCas, getCasSetComptes()))
            .getCorrections();

    log.info(
        "Recoupement: Patrimoine.nom={}, Corrections.size={}",
        plannedCas.patrimoine().nom(),
        corrections.size());

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
