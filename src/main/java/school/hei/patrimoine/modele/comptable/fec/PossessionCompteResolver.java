package school.hei.patrimoine.modele.comptable.fec;

import static java.time.LocalDate.now;
import static school.hei.patrimoine.modele.comptable.MouvementComptable.CREDIT;
import static school.hei.patrimoine.modele.comptable.MouvementComptable.DEBIT;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;

import org.jspecify.annotations.NonNull;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.CompteComptable;
import school.hei.patrimoine.modele.comptable.PairCompteComptable;
import school.hei.patrimoine.modele.possession.*;

public class PossessionCompteResolver {
  private static final Compte CAPITAL_SOCIAL =
      new Compte("Capital social", now(), Argent.ariary(0));
  private static final Compte COMPTE_ATTENTE =
      new Compte("Compte d'attente", now(), Argent.ariary(0));
  private static final Compte FINANCÉ = new Compte("Matériel", now(), Argent.ariary(0));

  public static PairCompteComptable resolve(Possession possession) {
    return switch (possession) {
      case FluxArgent flux -> getComptes(flux);
      case TransfertArgent transfert ->
          new PairCompteComptable(
              CompteComptable.builder()
                  .compte(transfert.getVersCompte())
                  .typeComptable(VIREMENT_INTERNE)
                  .mouvementComptable(DEBIT)
                  .build(),
              CompteComptable.builder()
                  .compte(transfert.getDepuisCompte())
                  .typeComptable(VIREMENT_INTERNE)
                  .mouvementComptable(CREDIT)
                  .build());
      case Compte compte ->
          new PairCompteComptable(
              CompteComptable.builder()
                  .compte(compte)
                  .typeComptable(BANQUE)
                  .mouvementComptable(DEBIT)
                  .build(),
              CompteComptable.builder()
                  .compte(CAPITAL_SOCIAL)
                  .typeComptable(BANQUE)
                  .mouvementComptable(CREDIT)
                  .build());
      case RemboursementDette remboursement ->
          new PairCompteComptable(
              CompteComptable.builder()
                  .compte(remboursement.getRemboursé())
                  .typeComptable(REMBOURSEMENT_DETTE)
                  .mouvementComptable(DEBIT)
                  .build(),
              CompteComptable.builder()
                  .compte(remboursement.getRembourseur())
                  .typeComptable(BANQUE)
                  .mouvementComptable(CREDIT)
                  .build());
      case AchatMaterielAuComptant achat ->
          new PairCompteComptable(
              CompteComptable.builder()
                  .compte(FINANCÉ)
                  .typeComptable(MATERIEL)
                  .mouvementComptable(DEBIT)
                  .build(),
              CompteComptable.builder()
                  .compte(achat.getFinanceur())
                  .typeComptable(BANQUE)
                  .mouvementComptable(CREDIT)
                  .build());
      default ->
          throw new IllegalArgumentException(
              "Impossible de déterminer les comptes pour " + possession.getClass().getSimpleName());
    };
  }

  private static @NonNull PairCompteComptable getComptes(FluxArgent flux) {
    if (flux.getFluxMensuel().montant() < 0) {
      return new PairCompteComptable(
          CompteComptable.builder()
              .compte(COMPTE_ATTENTE)
              .typeComptable(CCA)
              .mouvementComptable(DEBIT)
              .build(),
          CompteComptable.builder()
              .compte(flux.getCompte())
              .typeComptable(BANQUE)
              .mouvementComptable(CREDIT)
              .build());
    }
    return new PairCompteComptable(
        CompteComptable.builder()
            .compte(flux.getCompte())
            .typeComptable(BANQUE)
            .mouvementComptable(DEBIT)
            .build(),
        CompteComptable.builder()
            .compte(COMPTE_ATTENTE)
            .typeComptable(PCA)
            .mouvementComptable(CREDIT)
            .build());
  }
}
