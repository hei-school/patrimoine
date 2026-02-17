package school.hei.patrimoine.modele.normalizer;

import static java.lang.Double.parseDouble;
import static java.util.Locale.FRANCE;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import school.hei.patrimoine.modele.Argent;

public class ArgentNormalizer {
  private ArgentNormalizer() {}

  public static String normalize(Argent argent) {
    var symbols = new DecimalFormatSymbols(FRANCE);
    symbols.setGroupingSeparator('_');
    symbols.setDecimalSeparator('.');

    var df = new DecimalFormat("#,##0.##", symbols);
    df.setGroupingUsed(true);

    return String.format("%s%s", df.format(parseDouble(argent.ppMontant())), argent.devise());
  }
}
