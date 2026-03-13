package school.hei.patrimoine.modele.comptable;

import lombok.Builder;
import school.hei.patrimoine.modele.possession.Compte;

@Builder
public record CompteComptable(Compte compte, TypeComptable typeComptable, Sens sens) {
  public static CompteComptable of(Compte compte, TypeComptable typeComptable, Sens sens) {
    return CompteComptable.builder().compte(compte).typeComptable(typeComptable).sens(sens).build();
  }
}
