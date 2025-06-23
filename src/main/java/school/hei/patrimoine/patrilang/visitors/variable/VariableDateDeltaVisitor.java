package school.hei.patrimoine.patrilang.visitors.variable;

import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.R_VALUE_VARIABLE_NAME;

import java.time.LocalDate;
import school.hei.patrimoine.patrilang.modele.variable.Variable;

public class VariableDateDeltaVisitor implements VariableDeltaVisitor<LocalDate> {
  @Override
  public Variable<LocalDate> apply(LocalDate baseValue, VariableContext ctx) {
    var isMinus = nonNull(ctx.dateDelta().MOINS());
    var anneePart = visitAnneePart(ctx.dateDelta().anneePart());
    var moisPart = visitMoisPart(ctx.dateDelta().moisPart());
    var joursPart = visitJours(ctx.dateDelta().jourPart());
    LocalDate newValue;

    if (isMinus) {
      newValue = baseValue.minusYears(anneePart).minusMonths(moisPart).minusDays(joursPart);
    } else {
      newValue = baseValue.plusYears(anneePart).plusMonths(moisPart).plusDays(joursPart);
    }

    return new Variable<>(R_VALUE_VARIABLE_NAME, DATE, newValue);
  }

  private static int visitAnneePart(AnneePartContext ctx) {
    return isNull(ctx) ? 0 : parseInt(ctx.ENTIER().getText());
  }

  private static int visitMoisPart(MoisPartContext ctx) {
    return isNull(ctx) ? 0 : parseInt(ctx.ENTIER().getText());
  }

  private static int visitJours(JourPartContext ctx) {
    return isNull(ctx) ? 0 : parseInt(ctx.ENTIER().getText());
  }
}
