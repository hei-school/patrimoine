package school.hei.patrimoine.modele.possession.enumFEC;

import java.util.TimeZone;

public enum RegionComptable {
  FRANCE,
  MADAGASCAR,
  AUTRE;

  public static RegionComptable detecterRegion() {
    String zone = TimeZone.getDefault().getID();

    if (zone.startsWith("Africa/Antananarivo")) {
      return MADAGASCAR;
    }
    if (zone.startsWith("Europe/")) {
      return FRANCE;
    }
    return AUTRE;
  }
}
