package school.hei.patrimoine.modele.comptable;

import school.hei.patrimoine.modele.possession.Possession;

public record OperationComptable(Possession possession, TypeComptable typeComptable) {
  public OperationComptable(Possession possession) {
    this(possession, TypeComptable.from(possession));
  }
}
