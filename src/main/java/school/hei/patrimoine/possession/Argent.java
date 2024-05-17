package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Getter
public final class Argent extends Possession {
  private final Set<TrainDeVie> finances;

  public Argent(String nom, Instant t, int valeurComptable) {
    this(nom, t, valeurComptable, new HashSet<>());
  }

  Argent(String nom, Instant t, int valeurComptable, Set<TrainDeVie> finances) {
    super(nom, t, valeurComptable);
    this.finances = finances;
  }

    @Override
    public Argent projectionFuture(Instant tFutur) {
      return new Argent(
          nom,
          tFutur,
          valeurComptable - financementsFutur(tFutur),
          finances.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()));
    }

  int financementsFutur(Instant tFutur) {
    return finances.stream()
        .filter(trainDeVie -> trainDeVie.getDateDePonction() <= tFutur.getEpochSecond())
        .map(TrainDeVie::getDepensesMensuelle)
        .reduce(0, Integer::sum);
  }

  void addFinancés(TrainDeVie trainDeVie) {
    finances.add(trainDeVie);
  }
}
