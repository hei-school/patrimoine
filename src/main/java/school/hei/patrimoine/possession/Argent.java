package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class Argent extends Possession {
  private final Set<TrainDeVie> financés;

  public Argent(String nom, Instant t, int valeurComptable) {
    this(nom, t, valeurComptable, new HashSet<>());
  }

  private Argent(String nom, Instant t, int valeurComptable, Set<TrainDeVie> financés) {
    super(nom, t, valeurComptable);
    this.financés = financés;
  }

  @Override
  public Argent projectionFuture(Instant tFutur) {
    return new Argent(
        nom,
        tFutur,
        valeurComptable - financementsFutur(tFutur),
        financés.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()));
  }

  private int financementsFutur(Instant tFutur) {
    int totalFinancements = 0;
    for (TrainDeVie trainDeVie : financés) {
      Instant end = trainDeVie.getFin().isBefore(tFutur) ? trainDeVie.getFin() : tFutur;

      ZonedDateTime debutZoned = trainDeVie.getDebut().atZone(ZoneId.systemDefault());
      ZonedDateTime finZoned = end.atZone(ZoneId.systemDefault());
      int monthsBetween = (int) Duration.between(debutZoned, finZoned).toDays() / 30;

      totalFinancements += trainDeVie.getDepensesMensuelle() * monthsBetween;
    }
    return totalFinancements;
  }


  void addFinancés(TrainDeVie trainDeVie) {
    financés.add(trainDeVie);
  }
}
