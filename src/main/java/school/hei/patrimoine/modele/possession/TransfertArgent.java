package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.Devise;

public final class TransfertArgent extends Possession {
  private final GroupePossession transfertCommeGroupe;

  public TransfertArgent(
    String nom,
    Argent depuisArgent, Argent versArgent,
    LocalDate debut, LocalDate fin,
    int fluxMensuel, int dateOperation, Devise devise) {
    super(nom, debut, 0, devise);
    this.transfertCommeGroupe = new GroupePossession(
        nom,
        debut,
        Set.of(
            new FluxArgent("Flux TransfertArgent sortant: " + nom, depuisArgent, debut, fin, -1 * fluxMensuel, dateOperation, devise),
            new FluxArgent("Flux TransfertArgent entrant: " + nom, versArgent, debut, fin, fluxMensuel, dateOperation, devise)), devise);
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    return transfertCommeGroupe.projectionFuture(tFutur);
  }
}
