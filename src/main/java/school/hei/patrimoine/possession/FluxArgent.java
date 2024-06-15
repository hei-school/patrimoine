package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.Instant;

import static java.time.ZoneOffset.UTC;

@Getter
public final class FluxArgent extends Possession {
  private final Instant debut;
  private final Instant fin;
  private final int fluxMensuel;
  private final Argent argent;
  private final int dateOperation;

  public FluxArgent(
      String nom,
      int fluxMensuel,
      Instant debut,
      Instant fin,
      Argent argent,
      int dateOperation) {
    super(nom, null, 0);
    this.debut = debut;
    this.fin = fin;
    this.fluxMensuel = fluxMensuel;
    this.dateOperation = dateOperation;

    this.argent = argent;
    this.argent.addFinancés(this);
  }

  @Override
  public FluxArgent projectionFuture(Instant tFutur) {
    var tFuturBorneParFin = (tFutur.isBefore(fin)) ? tFutur : fin;
    var dateDeFinOperation = tFuturBorneParFin.atZone(UTC).toLocalDate();
    var dateDeDebutOperation = argent.t.atZone(UTC).toLocalDate();
    if (dateDeDebutOperation.isAfter(dateDeFinOperation)) {
      return this;
    }

    var nbOperations =
        (int)
            dateDeDebutOperation
                .datesUntil(dateDeFinOperation.plusDays(1))
                .filter(d -> d.getDayOfMonth() == dateOperation)
                .count();
    var argentFutur = new Argent(
        nom, tFutur, argent.getValeurComptable() + fluxMensuel * nbOperations);

    return new FluxArgent(nom, fluxMensuel, debut, tFuturBorneParFin, argentFutur, dateOperation);
  }
}
