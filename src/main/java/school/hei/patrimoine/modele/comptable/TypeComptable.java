package school.hei.patrimoine.modele.comptable;

import school.hei.patrimoine.modele.possession.*;

public enum TypeComptable {
  CCA,
  PRODUIT,
  IMMOBILISATION,
  CHARGE,
  AUTRE;

  public String codePCG() {
    return switch (this) {
      case IMMOBILISATION -> "2183";
      case CHARGE -> "600";
      case PRODUIT -> "700";
      case CCA -> "455";
      case AUTRE -> "512";
    };
  }

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
