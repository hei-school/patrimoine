package school.hei.patrimoine.modele.fec;

import static java.time.LocalDate.now;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;

import org.jspecify.annotations.NonNull;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.CompteComptable;
import school.hei.patrimoine.modele.possession.*;

public class PossessionCompteResolver {
  private static final Compte CAPITAL_SOCIAL =
      new Compte("Capital social", now(), Argent.ariary(0));
  private static final Compte COMPTE_ATTENTE =
      new Compte("Compte d'attente", now(), Argent.ariary(0));
  private static final Compte FINANCÉ = new Compte("Matériel", now(), Argent.ariary(0));

  public static Comptes resolve(Possession possession) {
    return switch (possession) {
      case FluxArgent flux -> getComptes(flux);
      case TransfertArgent transfert ->
          new Comptes(
              CompteComptable.of(transfert.versCompte(), VIREMENT_INTERNE),
              CompteComptable.of(transfert.depuisCompte(), VIREMENT_INTERNE));
      case Compte compte ->
          new Comptes(
              CompteComptable.of(compte, BANQUE), CompteComptable.of(CAPITAL_SOCIAL, BANQUE));
      case RemboursementDette remboursement ->
          new Comptes(
              CompteComptable.of(remboursement.remboursé(), REMBOURSEMENT_DETTE),
              CompteComptable.of(remboursement.rembourseur(), BANQUE));
      case AchatMaterielAuComptant achat ->
          new Comptes(
              CompteComptable.of(FINANCÉ, MATERIEL), CompteComptable.of(achat.financeur(), BANQUE));
      default ->
          throw new IllegalArgumentException(
              "Impossible de déterminer les comptes pour " + possession.getClass().getSimpleName());
    };
  }

  private static @NonNull Comptes getComptes(FluxArgent flux) {
    if (flux.getFluxMensuel().montant() < 0) {
      return new Comptes(
          CompteComptable.of(COMPTE_ATTENTE, CCA), CompteComptable.of(flux.getCompte(), BANQUE));
    }
    return new Comptes(
        CompteComptable.of(flux.getCompte(), BANQUE), CompteComptable.of(COMPTE_ATTENTE, PCA));
  }

  public record Comptes(CompteComptable compteDébiteur, CompteComptable compteCréditeur) {}
}
