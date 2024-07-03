package school.hei.patrimoine.modele.possession;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public final class Dette extends Argent {
  private LocalDate dateEcheance;

  public Dette(String nom, LocalDate dateCreation, int montant, LocalDate dateEcheance) {
    super(nom, dateCreation, montant);
    this.dateEcheance = dateEcheance;
    if (montant >= 0) {
      throw new IllegalArgumentException("Le montant de la dette doit être négatif.");
    }
  }

  @Override
  public Argent projectionFuture(LocalDate tFutur) {
    if (tFutur.isAfter(dateEcheance)) {
      return new Argent(nom, tFutur, 0);
    }
    return super.projectionFuture(tFutur);
  }
}
