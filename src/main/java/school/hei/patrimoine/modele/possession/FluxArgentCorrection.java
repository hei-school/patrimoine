package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;

public final class FluxArgentCorrection extends FluxArgent {
  public FluxArgentCorrection(
      String nom,
      Compte compte,
      LocalDate debut,
      LocalDate fin,
      int dateOperation,
      Argent fluxMensuel) {
    super(nom, compte, debut, fin, dateOperation, fluxMensuel);
  }

  public FluxArgentCorrection(String nom, Compte compte, LocalDate date, Argent montant) {
    super(nom, compte, date, montant);
  }
}
