package school.hei.patrimoine.modele.comptable;

import lombok.Builder;
import school.hei.patrimoine.modele.possession.Compte;

@Builder
public record CompteComptable(
    Compte compte, TypeComptable typeComptable, MouvementComptable mouvementComptable) {}
