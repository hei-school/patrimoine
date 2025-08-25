package school.hei.patrimoine.cas;

import static java.awt.EventQueue.invokeLater;
import static java.util.Comparator.comparing;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.objectif.ObjectifNonAtteint;
import school.hei.patrimoine.visualisation.swing.ihm.MainIHM;

@RequiredArgsConstructor
public class CasSetAnalyzer implements Consumer<CasSet> {
  private final int closeOperation;

  public static void main(String[] args) {
    new CasSetAnalyzer().accept(new CasSetSupplier().get());
  }

  public CasSetAnalyzer() {
    this(EXIT_ON_CLOSE);
  }

  @Override
  public void accept(CasSet casSet) {
    var aCas = casSet.set().stream().toList().getFirst();
    var patrimoineTout = new ToutCas(aCas.getAjd(), aCas.getFinSimulation(), casSet);
    verifie(patrimoineTout);
    visualise(casSet.set(), patrimoineTout);
  }

  private static void verifie(ToutCas patrimoineTout) {
    var objectifsNonAtteints = patrimoineTout.verifier();
    if (!objectifsNonAtteints.isEmpty()) {
      throw new RuntimeException(
          "Objectifs non atteints : "
              + objectifsNonAtteints.stream()
                  .sorted(
                      (lhs, rhs) -> {
                        if (lhs.objectif().t().equals(rhs.objectif().t())) {
                          return 0;
                        }
                        return lhs.objectif().t().isBefore(rhs.objectif().t()) ? -1 : 1;
                      })
                  .map(ObjectifNonAtteint::prettyPrint)
                  .collect(Collectors.joining("\n")));
    }
  }

  private void visualise(Set<Cas> casSet, ToutCas patrimoineTout) {
    invokeLater(
        () ->
            new MainIHM(
                Stream.concat(casSet.stream(), Set.of(patrimoineTout).stream())
                    .map(Cas::patrimoine)
                    .sorted(comparing(Patrimoine::nom))
                    .toList(),
                closeOperation));
  }
}
