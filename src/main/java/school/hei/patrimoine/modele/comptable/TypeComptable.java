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
      case FluxArgent fluxArgent -> resolveFlux(fluxArgent);
      default -> AUTRE;
    };
  }

  static TypeComptable resolveFlux(FluxArgent fluxArgent) {
    return fluxArgentDefaultTypeComptable(fluxArgent);
  }

  static TypeComptable fluxArgentDefaultTypeComptable(FluxArgent fluxArgent) {
    if (fluxArgent.getTypeComptable() != null) {
      return fluxArgent.getTypeComptable();
    }
    return fluxArgent.getFluxMensuel().lt(0) ? CHARGE : PRODUIT;
  }
}
