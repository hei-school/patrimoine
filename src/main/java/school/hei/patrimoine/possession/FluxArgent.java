package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;

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
    throw new NotImplemented();
  }
}
