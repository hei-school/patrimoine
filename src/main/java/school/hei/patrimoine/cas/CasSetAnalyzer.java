package school.hei.patrimoine.cas;

import static java.awt.EventQueue.invokeLater;
import static java.util.Comparator.comparing;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.objectif.ObjectifExeption;
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

  @SneakyThrows
  private static void verifie(ToutCas patrimoineTout) {
    var objectifsNonAtteints = patrimoineTout.verifier();
    if (!objectifsNonAtteints.isEmpty()) {
      throw new ObjectifExeption(objectifsNonAtteints);
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
