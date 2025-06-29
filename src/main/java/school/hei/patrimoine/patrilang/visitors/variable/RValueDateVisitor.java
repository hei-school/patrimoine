package school.hei.patrimoine.patrilang.visitors.variable;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.mapper.MonthTokenMapper.stringToMonth;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.R_VALUE_VARIABLE_NAME;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.patrilang.modele.variable.Variable;

@RequiredArgsConstructor
public class RValueDateVisitor implements RValueVariableVisitor<LocalDate> {
  private final VariableDateDeltaVisitor variableDateDeltaVisitor;

  @Override
  public Variable<LocalDate> apply(VariableContext ctx, VariableVisitor variableVisitor) {
    var baseDate = visitBaseDate(ctx.date(), variableVisitor);

    if (isNull(ctx.dateDelta())) {
      return new Variable<>(R_VALUE_VARIABLE_NAME, DATE, baseDate);
    }

    return variableDateDeltaVisitor.apply(baseDate, ctx, variableVisitor);
  }

  private static LocalDate visitBaseDate(DateContext ctx, VariableVisitor variableVisitor ) {
    if(nonNull(ctx.MOT_DATE_INDETERMINER())){
      return LocalDate.MAX;
    }

    if(nonNull(ctx.MOT_DATE_MAXIMUM())){
      return LocalDate.MAX;
    }

    if(nonNull(ctx.MOT_DATE_MINIMUM())){
      return LocalDate.MIN;
    }

    var jour = variableVisitor.asInt(ctx.jour);
    var annee = variableVisitor.asInt(ctx.annee);
    if(nonNull(ctx.moisEntier)){
      return LocalDate.of(annee, variableVisitor.asInt(ctx.moisEntier), jour );
    }

    return LocalDate.of(annee, stringToMonth(ctx.moisTextuel.getText()), jour);
  }
}
