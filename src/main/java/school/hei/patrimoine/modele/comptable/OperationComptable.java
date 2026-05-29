package school.hei.patrimoine.modele.comptable;

import static school.hei.patrimoine.modele.comptable.fec.PossessionCompteResolver.resolve;

import java.util.Optional;
import lombok.Getter;
import school.hei.patrimoine.modele.possession.Possession;

@Getter
public class OperationComptable {
  private final Possession possession;
  private final CompteComptable compteDebiteur;
  private final CompteComptable compteCrediteur;

  public OperationComptable(Possession possession, PairCompteComptable comptes) {
    this.possession = possession;
    this.compteDebiteur = comptes.compteDebiteur();
    this.compteCrediteur = comptes.compteCrediteur();
  }

  public static Optional<OperationComptable> of(Possession possession) {
    return resolve(possession).map(comptes -> new OperationComptable(possession, comptes));
  }
}
