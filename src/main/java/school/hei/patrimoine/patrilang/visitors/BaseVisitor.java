package school.hei.patrimoine.patrilang.visitors;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static school.hei.patrimoine.modele.Devise.*;

import java.time.LocalDate;
import org.antlr.v4.runtime.tree.TerminalNode;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.model.MaterielAppreciationType;

public class BaseVisitor {
  public static Argent visitArgent(PatriLangParser.ArgentContext ctx) {
    return new Argent(visitNombre(ctx.nombre()), visitDevise(ctx.DEVISE()));
  }

  public static Devise visitDevise(TerminalNode node) {
    return switch (parseNodeValue(node)) {
      case "Ar" -> MGA;
      case "€" -> EUR;
      case "$" -> CAD;
      default -> throw new IllegalArgumentException("Unknown devise type: " + parseNodeValue(node));
    };
  }

  public static LocalDate visitDate(PatriLangParser.DateContext ctx) {
    return LocalDate.of(
        parseInt(parseNodeValue(ctx.ENTIER(2))),
        parseInt(parseNodeValue(ctx.ENTIER(1))),
        parseInt(parseNodeValue(ctx.ENTIER(0))));
  }

  public static Double visitNombre(PatriLangParser.NombreContext ctx) {
    return parseDouble(ctx.getText());
  }

  public static double visitMaterielAppreciationFacteur(TerminalNode ctx) {
    return MaterielAppreciationType.fromString(parseNodeValue(ctx)).getFacteur();
  }

  public static String parseNodeValue(TerminalNode node) {
    return node.getText();
  }
}
