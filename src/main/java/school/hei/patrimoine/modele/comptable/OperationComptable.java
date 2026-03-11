package school.hei.patrimoine.modele.comptable;

import static school.hei.patrimoine.modele.fec.PossessionCompteResolver.resolve;

import lombok.Getter;
import school.hei.patrimoine.modele.possession.Possession;

@Getter
public class OperationComptable {
  private final Possession possession;
  private final CompteComptable compteActif;
  private final CompteComptable comptePassif;

  public OperationComptable(Possession possession) {
    this.possession = possession;
    var comptes = getComptesComptables();
    this.compteActif = comptes.compteActif;
    this.comptePassif = comptes.comptePassif;
  }

  public static OperationComptable make(Possession possession) {
    return new OperationComptable(possession);
  }

  private ComptesComptables getComptesComptables() {
    var comptes = resolve(possession);
    return new ComptesComptables(comptes.compteDébiteur(), comptes.compteCréditeur());
  }

  record ComptesComptables(CompteComptable compteActif, CompteComptable comptePassif) {}
}
