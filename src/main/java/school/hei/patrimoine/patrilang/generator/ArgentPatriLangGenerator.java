package school.hei.patrimoine.patrilang.generator;

import static java.lang.Double.parseDouble;
import static school.hei.patrimoine.patrilang.mapper.DeviseMapper.deviseToString;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import school.hei.patrimoine.modele.Argent;

public class ArgentPatriLangGenerator implements PatriLangGenerator<Argent> {
  @Override
  public String apply(Argent argent) {
    return String.format("%s%s", montant(argent), deviseToString(argent.devise()));
  }

  public static String montant(Argent argent) {
    var symbols = new DecimalFormatSymbols(Locale.FRANCE);
    symbols.setGroupingSeparator('_');
    symbols.setDecimalSeparator('.');

    var df = new DecimalFormat("#,##0.##", symbols);
    df.setGroupingUsed(true);

    return df.format(parseDouble(argent.ppMontant()));
  }
}
