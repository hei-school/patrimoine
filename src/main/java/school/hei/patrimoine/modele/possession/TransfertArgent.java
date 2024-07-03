package school.hei.patrimoine.modele.possession;

import school.hei.patrimoine.modele.Monnaie;

import java.time.LocalDate;
import java.util.Set;

public final class TransfertArgent extends Possession {
  private final GroupePossession transfertCommeGroupe;

  public TransfertArgent(
          String nom,
          Argent depuisArgent, Argent versArgent,
          LocalDate debut, LocalDate fin,
          int fluxMensuel, int dateOperation, Monnaie monnaie) {
    super(nom, debut, 0, null);
    this.transfertCommeGroupe = new GroupePossession(
        nom,
        debut,
        Set.of(
            new FluxArgent("Flux TransfertArgent sortant: " + nom, depuisArgent, debut, fin, -1 * fluxMensuel, dateOperation, depuisArgent.getMonnaie()),
            new FluxArgent("Flux TransfertArgent entrant: " + nom, versArgent, debut, fin, fluxMensuel, dateOperation, versArgent.getMonnaie())), monnaie);
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    return transfertCommeGroupe.projectionFuture(tFutur);
  }
}
