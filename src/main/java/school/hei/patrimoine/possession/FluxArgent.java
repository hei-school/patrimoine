package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public final class FluxArgent extends Possession {
  private final LocalDate debut;
  private final LocalDate fin;
  private final int fluxMensuel;
  private final Argent argent;
  private final int dateOperation;

  public FluxArgent(
      String nom,
      int fluxMensuel,
      LocalDate debut,
      LocalDate fin,
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
  public FluxArgent projectionFuture(LocalDate tFutur) {
    var tFuturBorneParFin = (tFutur.isBefore(fin)) ? tFutur : fin;
    var debutOperation = argent.t;
    if (debutOperation.isAfter(tFuturBorneParFin)) {
      return this;
    }

    var nbOperations =
        (int)
            debutOperation
                .datesUntil(tFuturBorneParFin.plusDays(1))
                .filter(d -> d.getDayOfMonth() == dateOperation)
                .count();
    var argentFutur = new Argent(
        nom, tFutur, argent.getValeurComptable() + fluxMensuel * nbOperations);

    return new FluxArgent(nom, fluxMensuel, debut, tFuturBorneParFin, argentFutur, dateOperation);
  }
}
