package school.hei.patrimoine.modele.decomposeur;

import static school.hei.patrimoine.modele.normalizer.PossessionNomNormalizer.normalize;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class IdRetriever {
  private static final String FLUX_ARGENT_DATE_SEPARATEUR = "__du_";
  private static final Pattern MULTIPLE_REALISATION_PATTERN = Pattern.compile("\\[(.*)]__(.*)");
  private static final Pattern DECOMPOSED_ID_PATTERN =
      Pattern.compile("^(.*)__du_(\\d{4}_\\d{2}_\\d{2})$");

  public static String getDecomposedId(String baseId, LocalDate date) {
    return normalize(baseId + FLUX_ARGENT_DATE_SEPARATEUR + date);
  }

  public static String getBaseIdFromDecomposedId(String decomposedId) {
    var matcher = DECOMPOSED_ID_PATTERN.matcher(decomposedId);
    if (!matcher.matches()) {
      return decomposedId;
    }
    return matcher.group(1);
  }

  public static String getPlannedIdFromRealisationId(String realisationId) {
    var plannedId = realisationId;
    var matcher = MULTIPLE_REALISATION_PATTERN.matcher(realisationId);
    if (matcher.matches()) {
      plannedId = matcher.group(1);
    }

    return plannedId;
  }
}
