package school.hei.patrimoine.modele.fec;

import static java.time.LocalDate.now;
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
              CompteComptable.of(transfert.getVersCompte(), CCA, true),
              CompteComptable.of(transfert.getDepuisCompte(), CCA, false));
      case Compte compte ->
          new Comptes(
              CompteComptable.of(compte, CCA, true),
              CompteComptable.of(CAPITAL_SOCIAL, CCA, false));
      case RemboursementDette remboursement ->
          new Comptes(
              CompteComptable.of(remboursement.getRemboursé(), CCA, true),
              CompteComptable.of(remboursement.getRembourseur(), CCA, false));
      case AchatMaterielAuComptant achat ->
          new Comptes(
              CompteComptable.of(FINANCÉ, CCA, true),
              CompteComptable.of(achat.getFinanceur(), CCA, false));
      default ->
          throw new IllegalArgumentException(
              "Impossible de déterminer les comptes pour " + possession.getClass().getSimpleName());
    };
  }

  private static @NonNull Comptes getComptes(FluxArgent flux) {
    if (flux.getFluxMensuel().montant() < 0) {
      return new Comptes(
          CompteComptable.of(COMPTE_ATTENTE, CCA, true),
          CompteComptable.of(flux.getCompte(), CCA, false));
    }
    return new Comptes(
        CompteComptable.of(flux.getCompte(), CCA, true),
        CompteComptable.of(COMPTE_ATTENTE, CCA, false));
  }

  public record Comptes(CompteComptable compteDébiteur, CompteComptable compteCréditeur) {}
}
