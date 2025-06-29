package school.hei.patrimoine.patrilang.visitors;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static school.hei.patrimoine.modele.Devise.*;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;

import org.antlr.v4.runtime.tree.TerminalNode;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.PersonneMorale;
import school.hei.patrimoine.patrilang.modele.DateFin;
import school.hei.patrimoine.patrilang.modele.MaterielAppreciationType;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class BaseVisitor {
  public static Personne visitPersonne(TextContext ctx) {
    return new Personne(parseNodeValue(ctx.TEXT()));
  }

  public static PersonneMorale visitPersonneMorale(TextContext ctx) {
    return new PersonneMorale(parseNodeValue(ctx.TEXT()));
  }

  public static DateFin visitDateFin(DateFinContext ctx, VariableVisitor variableVisitor) {
    int dateDOpération = parseInt(parseNodeValue(ctx.ENTIER()));
    var dateFinValue = variableVisitor.asDate(ctx.dateValue);

    return new DateFin(dateDOpération, dateFinValue);
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

  public static Double visitNombre(NombreContext ctx) {
    return parseDouble(ctx.getText().replaceAll("_", ""));
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
