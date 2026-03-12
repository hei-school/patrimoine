package school.hei.patrimoine.modele.comptable;

import static school.hei.patrimoine.modele.fec.PossessionCompteResolver.resolve;

import lombok.Getter;
import school.hei.patrimoine.modele.possession.Possession;

@Getter
public class OperationComptable {
  private final Possession possession;
  private final CompteComptable compteDebiteur;
  private final CompteComptable compteCrediteur;

  public OperationComptable(Possession possession) {
    this.possession = possession;
    var comptes = getComptesComptables();
    this.compteDebiteur = comptes.compteDebiteur;
    this.compteCrediteur = comptes.compteCrediteur;
  }

  public static OperationComptable make(Possession possession) {
    return new OperationComptable(possession);
  }

  private ComptesComptables getComptesComptables() {
    var comptes = resolve(possession);
    return new ComptesComptables(comptes.compteDébiteur(), comptes.compteCréditeur());
  }

  record ComptesComptables(CompteComptable compteDebiteur, CompteComptable compteCrediteur) {}
}
