package school.hei.patrimoine.modele.comptable.fec;

import static java.time.LocalDate.now;
import static school.hei.patrimoine.modele.comptable.Sens.CREDIT;
import static school.hei.patrimoine.modele.comptable.Sens.DEBIT;
import static school.hei.patrimoine.modele.comptable.TypeComptable.CCA;

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
              CompteComptable.of(transfert.getVersCompte(), CCA, DEBIT),
              CompteComptable.of(transfert.getDepuisCompte(), CCA, CREDIT));
      case Compte compte ->
          new Comptes(
              CompteComptable.of(compte, CCA, DEBIT),
              CompteComptable.of(CAPITAL_SOCIAL, CCA, CREDIT));
      case RemboursementDette remboursement ->
          new Comptes(
              CompteComptable.of(remboursement.getRemboursé(), CCA, DEBIT),
              CompteComptable.of(remboursement.getRembourseur(), CCA, CREDIT));
      case AchatMaterielAuComptant achat ->
          new Comptes(
              CompteComptable.of(FINANCÉ, CCA, DEBIT),
              CompteComptable.of(achat.getFinanceur(), CCA, CREDIT));
      default ->
          throw new IllegalArgumentException(
              "Impossible de déterminer les comptes pour " + possession.getClass().getSimpleName());
    };
  }

  private static @NonNull Comptes getComptes(FluxArgent flux) {
    if (flux.getFluxMensuel().montant() < 0) {
      return new Comptes(
          CompteComptable.of(COMPTE_ATTENTE, CCA, DEBIT),
          CompteComptable.of(flux.getCompte(), CCA, CREDIT));
    }
    return new Comptes(
        CompteComptable.of(flux.getCompte(), CCA, DEBIT),
        CompteComptable.of(COMPTE_ATTENTE, CCA, CREDIT));
  }

  public record Comptes(CompteComptable compteDébiteur, CompteComptable compteCréditeur) {}
}
