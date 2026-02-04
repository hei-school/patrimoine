package school.hei.patrimoine.modele.decomposeur;

import school.hei.patrimoine.modele.possession.*;

public class PossessionCompteResolver {
  public static Comptes resolve(Possession possession) {
    return switch (possession) {
      case FluxArgent flux -> new Comptes(flux.getCompte(), null);
      case TransfertArgent transfert ->
          new Comptes(transfert.depuisCompte(), transfert.versCompte());
      case Compte compte -> new Comptes(compte, null);
      case RemboursementDette remboursement ->
          new Comptes(remboursement.rembourseur(), remboursement.remboursé());
      case AchatMaterielAuComptant achat -> new Comptes(achat.financeur(), null);
      default ->
          throw new IllegalArgumentException(
              "Impossible de déterminer les comptes pour " + possession.getClass().getSimpleName());
    };
  }

  public record Comptes(Compte comptePrincipal, Compte compteAuxiliaire) {}
}
