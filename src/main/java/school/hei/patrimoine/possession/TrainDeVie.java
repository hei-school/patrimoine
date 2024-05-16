package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public final class TrainDeVie extends Possession {
  private final Instant debut;
  private final Instant fin;
  @Getter
  private final int depensesMensuelle;
  private final int dateDePonction;
  @Getter
  private final Argent financePar;

  public TrainDeVie(
      String nom,
      int depensesMensuelle,
      Instant debut,
      Instant fin,
      Argent financePar,
      int dateDePonction) {
    super(nom, null, 0);
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.dateDePonction = dateDePonction;

    this.financePar = financePar;
    this.financePar.addFinancés(this);
  }
  public static int calculRepetitionDeDate(LocalDate debut, LocalDate fin, int jour) {
    int count = 0;
    LocalDate current = debut;
    while (!current.isAfter(fin)) {
      if (current.getDayOfMonth() == jour) {
        count++;
      }
      current = current.plusMonths(1);
    }
    return count;
  }

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
    LocalDate finDePonctionnement = LocalDate.ofInstant(tFutur.isAfter(fin) ? fin : tFutur, ZoneId.systemDefault());
    long differenceDeMois = calculRepetitionDeDate(
            LocalDate.ofInstant(debut, ZoneId.systemDefault()),
            finDePonctionnement,
            dateDePonction
    );

    if(finDePonctionnement.getDayOfMonth() > dateDePonction)
      differenceDeMois++;

    return new TrainDeVie(nom,tFutur.isAfter(fin) ? 0 : depensesMensuelle,debut,fin,
            new Argent(
                    financePar.nom,
                    tFutur,
                    financePar.valeurComptable - depensesMensuelle * (int) differenceDeMois),
            dateDePonction
    );
  }
}
