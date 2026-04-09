package school.hei.patrimoine.modele.comptable;

import static school.hei.patrimoine.modele.comptable.fec.PossessionCompteResolver.resolve;

import lombok.Getter;
import school.hei.patrimoine.modele.possession.Possession;

@Getter
public class OperationComptable {
  private final Possession possession;
  private final CompteComptable compteDebiteur;
  private final CompteComptable compteCrediteur;

  public OperationComptable(Possession possession) {
    this.possession = possession;
    var comptes = resolve(possession);
    this.compteDebiteur = comptes.compteDebiteur();
    this.compteCrediteur = comptes.compteCrediteur();
  }
}
