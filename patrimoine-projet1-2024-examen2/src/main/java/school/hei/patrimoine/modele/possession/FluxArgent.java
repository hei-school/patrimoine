package school.hei.patrimoine.modele.possession;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public final class FluxArgent extends Possession {
  private final Argent argent;
  private final LocalDate debut;
  private final LocalDate fin;
  private final int fluxMensuel;
  private final int dateOperation;

  public FluxArgent(
      String nom, Argent argent, LocalDate debut, LocalDate fin, int fluxMensuel, int dateOperation) {
    super(nom, null, 0);
    this.argent = argent;
    this.argent.addFinancés(this);

    this.debut = debut;
    this.fin = fin;
    this.fluxMensuel = fluxMensuel;
    this.dateOperation = dateOperation;
  }

  @Override
  public FluxArgent projectionFuture(LocalDate tFutur) {
    var tFuturMajoréParFin = (tFutur.isBefore(fin)) ? tFutur : fin;
    var debutOperationMinoréParDebut = argent.t.isBefore(debut) ? debut : argent.t;
    if (debutOperationMinoréParDebut.isAfter(tFuturMajoréParFin)) {
      return this;
    }

    var nbOperations =
        (int)
            debutOperationMinoréParDebut
                .datesUntil(tFuturMajoréParFin.plusDays(1))
                .filter(d -> d.getDayOfMonth() == dateOperation)
                .count();
    var argentFutur = new Argent(
        nom, tFutur, argent.getValeurComptable() + fluxMensuel * nbOperations);

    return new FluxArgent(nom, argentFutur, debut, tFuturMajoréParFin, fluxMensuel, dateOperation);
  }
}
