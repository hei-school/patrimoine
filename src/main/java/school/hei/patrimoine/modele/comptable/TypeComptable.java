package school.hei.patrimoine.modele.comptable;

import school.hei.patrimoine.modele.possession.*;

public enum TypeComptable {
  CCA,
  PRODUIT,
  IMMOBILISATION,
  CHARGE,
  AUTRE;

  public static TypeComptable from(Possession possession) {
    return switch (possession) {
      case RemboursementDette ignored -> CHARGE;
      case Materiel ignored -> IMMOBILISATION;
      case AchatMaterielAuComptant ignored -> IMMOBILISATION;
      case FluxArgent fluxArgent -> fluxArgent.getFluxMensuel().lt(0) ? CHARGE : PRODUIT;
      default -> AUTRE;
    };
  }
}
