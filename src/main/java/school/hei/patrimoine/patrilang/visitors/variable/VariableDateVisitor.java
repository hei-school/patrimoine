package school.hei.patrimoine.patrilang.visitors.variable;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.mapper.MonthMapper.stringToMonth;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.extractVariableName;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VariableDateVisitor {
  public LocalDate apply(DateContext ctx, VariableVisitor variableVisitor) {
    var baseDate = visitBaseDate(ctx.dateAtom(), variableVisitor);

    if (isNull(ctx.dateDelta())) {
      return baseDate;
    }

    return applyDelta(baseDate, ctx.dateDelta(), variableVisitor);
  }

  private static LocalDate visitBaseDate(DateAtomContext ctx, VariableVisitor variableVisitor) {
    if (nonNull(ctx.MOT_DATE_INDETERMINER())) {
      return LocalDate.MAX;
    }

    if (nonNull(ctx.MOT_DATE_MAXIMUM())) {
      return LocalDate.MAX;
    }

    if (nonNull(ctx.MOT_DATE_MINIMUM())) {
      return LocalDate.MIN;
    }

    if (nonNull(ctx.DATE_VARIABLE())) {
      var name = extractVariableName(ctx.DATE_VARIABLE().getText());
      return (LocalDate) variableVisitor.getVariableScope().get(name, DATE).value();
    }

    var jour = variableVisitor.asInt(ctx.jour);
    var annee = variableVisitor.asInt(ctx.annee);
    if (nonNull(ctx.moisEntier)) {
      return LocalDate.of(annee, variableVisitor.asInt(ctx.moisEntier), jour);
    }

    return LocalDate.of(annee, stringToMonth(ctx.moisTextuel.getText()), jour);
  }

  private static LocalDate applyDelta(
      LocalDate baseValue, DateDeltaContext ctx, VariableVisitor variableVisitor) {
    var isMinus = nonNull(ctx.MOINS());
    var anneePart = visitAnneePart(ctx.anneePart(), variableVisitor);
    var moisPart = visitMoisPart(ctx.moisPart(), variableVisitor);
    var joursPart = visitJours(ctx.jourPart(), variableVisitor);
    LocalDate newValue;

    if (isMinus) {
      newValue = baseValue.minusYears(anneePart).minusMonths(moisPart).minusDays(joursPart);
    } else {
      newValue = baseValue.plusYears(anneePart).plusMonths(moisPart).plusDays(joursPart);
    }

    return newValue;
  }

  private static int visitAnneePart(AnneePartContext ctx, VariableVisitor variableVisitor) {
    return isNull(ctx) ? 0 : variableVisitor.asInt((ctx.variable()));
  }

  private static int visitMoisPart(MoisPartContext ctx, VariableVisitor variableVisitor) {
    return isNull(ctx) ? 0 : variableVisitor.asInt((ctx.variable()));
  }

  private static int visitJours(JourPartContext ctx, VariableVisitor variableVisitor) {
    return isNull(ctx) ? 0 : variableVisitor.asInt((ctx.variable()));
  }
}
