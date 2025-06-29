package school.hei.patrimoine.patrilang.visitors.variable;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.R_VALUE_VARIABLE_NAME;

import java.time.LocalDate;
import school.hei.patrimoine.patrilang.modele.variable.Variable;

public class VariableDateDeltaVisitor implements VariableDeltaVisitor<LocalDate> {
  @Override
  public Variable<LocalDate> apply(LocalDate baseValue, VariableContext ctx, VariableVisitor variableVisitor) {
    var isMinus = nonNull(ctx.dateDelta().MOINS());
    var anneePart = visitAnneePart(ctx.dateDelta().anneePart(), variableVisitor);
    var moisPart = visitMoisPart(ctx.dateDelta().moisPart(), variableVisitor);
    var joursPart = visitJours(ctx.dateDelta().jourPart(), variableVisitor);
    LocalDate newValue;

    if (isMinus) {
      newValue = baseValue.minusYears(anneePart).minusMonths(moisPart).minusDays(joursPart);
    } else {
      newValue = baseValue.plusYears(anneePart).plusMonths(moisPart).plusDays(joursPart);
    }

    return new Variable<>(R_VALUE_VARIABLE_NAME, DATE, newValue);
  }

  private int visitAnneePart(AnneePartContext ctx, VariableVisitor variableVisitor) {
    return isNull(ctx) ? 0 : variableVisitor.asInt((ctx.variable()));
  }

  private int visitMoisPart(MoisPartContext ctx, VariableVisitor variableVisitor) {
    return isNull(ctx) ? 0 : variableVisitor.asInt((ctx.variable()));
  }

  private int visitJours(JourPartContext ctx, VariableVisitor variableVisitor) {
    return isNull(ctx) ? 0 : variableVisitor.asInt((ctx.variable()));
  }
}
