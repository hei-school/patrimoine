package school.hei.patrimoine.modele.fec;

import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.possession.*;

public class PossessionCompteResolver {
  public static Comptes resolve(OperationComptable operation) {
    return switch (operation.possession()) {
      case FluxArgent flux ->
          flux.getFluxMensuel().montant() < 0
              ? new Comptes(flux.compteAttente(), flux.getCompte())
              : new Comptes(flux.getCompte(), flux.compteAttente());
      case TransfertArgent transfert ->
          new Comptes(transfert.versCompte(), transfert.depuisCompte());
      case Compte compte -> new Comptes(compte, compte.capitalSocial());
      case RemboursementDette remboursement ->
          new Comptes(remboursement.remboursé(), remboursement.rembourseur());
      case AchatMaterielAuComptant achat -> new Comptes(achat.financé(), achat.financeur());
      default ->
          throw new IllegalArgumentException(
              "Impossible de déterminer les comptes pour " + operation.getClass().getSimpleName());
    };
  }

  public record Comptes(Compte compteDébiteur, Compte compteCréditeur) {}
}
