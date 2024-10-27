package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.possession.TypeAgregat.CORRECTION;

import java.time.LocalDate;
import lombok.Getter;
import school.hei.patrimoine.modele.Devise;

public final class CompteCorrection extends Possession {

  @Getter private final Argent argent;

  public CompteCorrection(String nom, Devise devise) {
    this(
        "Correction[" + nom + "]",
        new Argent(String.format("Correction.Argent[%s]", nom), LocalDate.MIN, 0, devise));
  }

  private CompteCorrection(String nom, Argent argent) {
    super(nom, argent.t, argent.valeurComptable, argent.devise);
    this.argent = argent;
  }

  @Override
  public CompteCorrection projectionFuture(LocalDate tFutur) {
    return new CompteCorrection(nom, argent.projectionFuture(tFutur));
  }

  @Override
  public TypeAgregat typeAgregat() {
    return CORRECTION;
  }
}
