package school.hei.patrimoine.modele.fec;

import static java.time.LocalDate.now;

import org.jspecify.annotations.NonNull;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.possession.*;

public class PossessionCompteResolver {
  private static final Compte CAPITAL_SOCIAL =
      new Compte("Capital social", now(), Argent.ariary(0));
  private static final Compte COMPTE_ATTENTE =
      new Compte("Compte d'attente", now(), Argent.ariary(0));
  private static final Compte FINANCÉ = new Compte("Matériel", now(), Argent.ariary(0));

  public static Comptes resolve(OperationComptable operation) {
    return switch (operation.possession()) {
      case FluxArgent flux -> getComptes(flux);
      case TransfertArgent transfert ->
          new Comptes(transfert.versCompte(), transfert.depuisCompte());
      case Compte compte -> new Comptes(compte, CAPITAL_SOCIAL);
      case RemboursementDette remboursement ->
          new Comptes(remboursement.remboursé(), remboursement.rembourseur());
      case AchatMaterielAuComptant achat -> new Comptes(FINANCÉ, achat.financeur());
      default ->
          throw new IllegalArgumentException(
              "Impossible de déterminer les comptes pour " + operation.getClass().getSimpleName());
    };
  }

  private static @NonNull Comptes getComptes(FluxArgent flux) {
    if (flux.getFluxMensuel().montant() < 0) {
      return new Comptes(COMPTE_ATTENTE, flux.getCompte());
    }
    return new Comptes(flux.getCompte(), COMPTE_ATTENTE);
  }

  public record Comptes(Compte compteDébiteur, Compte compteCréditeur) {}
}
