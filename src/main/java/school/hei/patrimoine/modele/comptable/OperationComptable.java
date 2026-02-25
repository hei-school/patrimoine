package school.hei.patrimoine.modele.comptable;

import school.hei.patrimoine.modele.possession.Possession;

public record OperationComptable(Possession possession, TypeComptable typeComptable) {
  public OperationComptable(Possession possession, TypeComptable typeComptable) {
    this.possession = possession;
    this.typeComptable = typeComptable;
  }

  public static OperationComptable make(Possession possession) {
    return new OperationComptable(possession, TypeComptable.from(possession));
  }
}
