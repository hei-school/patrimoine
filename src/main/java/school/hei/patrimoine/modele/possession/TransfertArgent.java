package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.Devise.NON_NOMMEE;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.Devise;

public final class TransfertArgent extends Possession {
  private final GroupePossession transfertCommeGroupe;

  public TransfertArgent(
      String nom,
      Argent depuisArgent,
      Argent versArgent,
      LocalDate debut,
      LocalDate fin,
      int fluxMensuel,
      int dateOperation) {
    this(nom, depuisArgent, versArgent, debut, fin, fluxMensuel, dateOperation, NON_NOMMEE);
  }

  public TransfertArgent(
      String nom,
      Argent depuisArgent,
      Argent versArgent,
      LocalDate debut,
      LocalDate fin,
      int fluxMensuel,
      int dateOperation,
      Devise devise) {
    super(nom, debut, 0, devise);
    this.transfertCommeGroupe =
        new GroupePossession(
            nom,
            debut,
            Set.of(
                new FluxArgent(
                    "Flux TransfertArgent sortant: " + nom,
                    depuisArgent,
                    debut,
                    fin,
                    -1 * fluxMensuel,
                    dateOperation,
                    devise),
                new FluxArgent(
                    "Flux TransfertArgent entrant: " + nom,
                    versArgent,
                    debut,
                    fin,
                    fluxMensuel,
                    dateOperation,
                    devise)),
            devise);
  }

  public TransfertArgent(String nom, Argent depuis, Argent vers, LocalDate date, int montant) {
    this(nom, depuis, vers, date, date, montant, date.getDayOfMonth());
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    return transfertCommeGroupe.projectionFuture(tFutur);
  }
}
