package school.hei.patrimoine.modele.comptable;

import lombok.Builder;
import school.hei.patrimoine.modele.possession.Compte;

@Builder
public record CompteComptable(Compte compte, TypeComptable typeComptable, boolean isDebit) {
  public static CompteComptable of(Compte compte, TypeComptable typeComptable, boolean isDebit) {
    return CompteComptable.builder()
        .compte(compte)
        .typeComptable(typeComptable)
        .isDebit(isDebit)
        .build();
  }
}
