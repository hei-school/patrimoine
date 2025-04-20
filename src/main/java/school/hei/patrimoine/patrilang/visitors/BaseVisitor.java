package school.hei.patrimoine.patrilang.visitors;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static school.hei.patrimoine.modele.Devise.*;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateFinContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.NombreContext;

import java.time.LocalDate;
import org.antlr.v4.runtime.tree.TerminalNode;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.patrilang.modele.DateFin;
import school.hei.patrimoine.patrilang.modele.MaterielAppreciationType;

public class BaseVisitor {
  public static Argent visitArgent(ArgentContext ctx) {
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

  public static LocalDate visitDate(DateContext ctx) {
    return LocalDate.of(
        parseInt(parseNodeValue(ctx.ENTIER(2))),
        parseInt(parseNodeValue(ctx.ENTIER(1))),
        parseInt(parseNodeValue(ctx.ENTIER(0))));
  }

  public static Double visitNombre(NombreContext ctx) {
    return parseDouble(ctx.getText());
  }

  public static double visitMaterielAppreciationFacteur(TerminalNode ctx) {
    return MaterielAppreciationType.fromString(parseNodeValue(ctx)).getFacteur();
  }

  public static DateFin visitDateFin(DateFinContext ctx) {
    int dateDOpération = parseInt(parseNodeValue(ctx.ENTIER()));
    LocalDate dateFinValue =
        ctx.dateFinValue().date() == null ? LocalDate.MAX : visitDate(ctx.dateFinValue().date());

    return new DateFin(dateDOpération, dateFinValue);
  }

  public static String parseNodeValue(TerminalNode node) {
    return node.getText();
  }
}
