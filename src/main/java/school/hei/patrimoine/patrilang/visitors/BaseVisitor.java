package school.hei.patrimoine.patrilang.visitors;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;
import static school.hei.patrimoine.modele.Devise.*;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariable;

import java.time.LocalDate;
import org.antlr.v4.runtime.tree.TerminalNode;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.patrilang.modele.DateFin;
import school.hei.patrimoine.patrilang.modele.MaterielAppreciationType;

public class BaseVisitor {
  public static Argent visitArgent(ArgentContext ctx) {
    return new Argent(visitNombre(ctx.nombre()), visitDevise(ctx.devise()));
  }

  public static Devise visitDevise(DeviseContext ctx) {
    return switch (parseNodeValue(ctx.DEVISE())) {
      case "Ar" -> MGA;
      case "€" -> EUR;
      case "$" -> CAD;
      default ->
          throw new IllegalArgumentException(
              "Unknown devise type: " + parseNodeValue(ctx.DEVISE()));
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

  public static LocalDate visitDateFinValue(DateFinValueContext ctx) {
    return isNull(ctx.date()) ? LocalDate.MAX : visitDate(ctx.date());
  }

  public static DateFin visitDateFin(DateFinContext ctx) {
    int dateDOpération = parseInt(parseNodeValue(ctx.ENTIER()));
    var dateFinValue =
        visitVariable(ctx.variable(), DateFinValueContext.class, BaseVisitor::visitDateFinValue);

    return new DateFin(dateDOpération, dateFinValue);
  }

  public static String visitText(TextContext ctx) {
    return parseNodeValue(ctx.TEXT());
  }

  public static String parseNodeValue(TerminalNode node) {
    return node.getText();
  }
}
