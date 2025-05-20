package school.hei.patrimoine.patrilang.visitors;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;
import static school.hei.patrimoine.modele.Devise.*;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;

import java.time.LocalDate;
import org.antlr.v4.runtime.tree.TerminalNode;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.patrilang.modele.DateFin;
import school.hei.patrimoine.patrilang.modele.MaterielAppreciationType;

public class BaseVisitor {

  public static Personne visitPersonne(TextContext ctx) {
    return new Personne(parseNodeValue(ctx.TEXT()));
  }

  public static DateFin visitDateFin(
      DateFinContext ctx, VariableVisitor<DateContext, LocalDate> variableDateVisitor) {
    int dateDOpération = parseInt(parseNodeValue(ctx.ENTIER()));
    var dateFinValue = variableDateVisitor.apply(ctx.variable());

    return new DateFin(dateDOpération, dateFinValue);
  }

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
    if (!isNull(ctx.MOT_DATE_INDETERMINER())) {
      return LocalDate.MAX;
    }

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

  public static String visitText(TextContext ctx) {
    return parseNodeValue(ctx.TEXT());
  }

  public static String parseNodeValue(TerminalNode node) {
    return node.getText();
  }
}
