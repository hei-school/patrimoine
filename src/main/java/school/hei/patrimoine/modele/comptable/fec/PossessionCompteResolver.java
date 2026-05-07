package school.hei.patrimoine.modele.comptable.fec;

import static java.time.LocalDate.now;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.MouvementComptable.CREDIT;
import static school.hei.patrimoine.modele.comptable.MouvementComptable.DEBIT;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;

import java.util.Optional;
import org.jspecify.annotations.NonNull;
import school.hei.patrimoine.modele.comptable.CompteComptable;
import school.hei.patrimoine.modele.comptable.PairCompteComptable;
import school.hei.patrimoine.modele.possession.*;

public class PossessionCompteResolver {
  private static final Compte FINANCÉ = new Compte("Matériel", now(), ariary(0));
  private static final Compte CAPITAL_SOCIAL = new Compte("Capital social", now(), ariary(0));

  public static Optional<PairCompteComptable> resolve(Possession possession) {
    var pairCompte =
        switch (possession) {
          case FluxArgent flux -> getComptes(flux);
          case TransfertArgent transfert ->
              new PairCompteComptable(
                  CompteComptable.builder()
                      .compte(transfert.getVersCompte())
                      .typeComptable(CHARGE_DIVERSE)
                      .mouvementComptable(DEBIT)
                      .build(),
                  CompteComptable.builder()
                      .compte(transfert.getDepuisCompte())
                      .typeComptable(BANQUE)
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
                      .typeComptable(CAPITAL)
                      .mouvementComptable(CREDIT)
                      .build());
          case RemboursementDette remboursement ->
              new PairCompteComptable(
                  CompteComptable.builder()
                      .compte(remboursement.getRemboursé())
                      .typeComptable(DETTE)
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
          default -> null;
        };
    return Optional.ofNullable(pairCompte);
  }

  private static @NonNull PairCompteComptable getComptes(FluxArgent flux) {
    if (flux.getFluxMensuel().montant() < 0) {
      var compteCCA = new Compte("CCA_" + flux.nom(), now(), ariary(0));
      return new PairCompteComptable(
          CompteComptable.builder()
              .compte(compteCCA)
              .typeComptable(CCA)
              .mouvementComptable(DEBIT)
              .build(),
          CompteComptable.builder()
              .compte(flux.getCompte())
              .typeComptable(BANQUE)
              .mouvementComptable(CREDIT)
              .build());
    }
    var comptePCA = new Compte("PCA_" + flux.nom(), now(), ariary(0));
    return new PairCompteComptable(
        CompteComptable.builder()
            .compte(flux.getCompte())
            .typeComptable(BANQUE)
            .mouvementComptable(DEBIT)
            .build(),
        CompteComptable.builder()
            .compte(comptePCA)
            .typeComptable(PCA)
            .mouvementComptable(CREDIT)
            .build());
  }
}
