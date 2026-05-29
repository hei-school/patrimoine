package school.hei.patrimoine.modele.comptable.fec;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.*;

public class PossessionFluxResolver {
  public static Argent resolve(Possession possession) {
    return switch (possession) {
      case FluxArgent flux -> flux.getFluxMensuel();
      case TransfertArgent transfert -> transfert.getFluxMensuel();
      case Compte compte -> compte.valeurComptable();
      case RemboursementDette remboursement -> remboursement.getMontant();
      case AchatMaterielAuComptant achat -> achat.getValeurComptableALAchat();
      default ->
          throw new IllegalArgumentException(
              "Impossible de déterminer les flux pour " + possession.getClass().getSimpleName());
    };
  }
}
