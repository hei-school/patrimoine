package school.hei.patrimoine.visualisation.swing.ihm.google.utils.formatter;

import static java.lang.Double.parseDouble;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import school.hei.patrimoine.modele.Argent;

public class ArgentFormatter {
  public static String format(Argent argent) {
    return String.format("%s %s", montant(argent), argent.devise());
  }

  public static String montant(Argent argent) {
    var symbols = new DecimalFormatSymbols(Locale.FRANCE);
    symbols.setGroupingSeparator(' ');
    var df = new DecimalFormat("#,###", symbols);
    return df.format(parseDouble(argent.ppMontant()));
  }
}
