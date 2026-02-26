package school.hei.patrimoine.modele.comptable;

import school.hei.patrimoine.modele.possession.Possession;

public record OperationComptable(Possession possession, TypeComptable type) {
  public OperationComptable(Possession possession, TypeComptable type) {
    this.possession = possession;
    this.type = type;
  }

  public static OperationComptable make(Possession possession) {
    return new OperationComptable(possession, TypeComptable.from(possession));
  }
}
