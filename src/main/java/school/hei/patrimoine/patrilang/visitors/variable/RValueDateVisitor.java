package school.hei.patrimoine.patrilang.visitors.variable;

import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.R_VALUE_VARIABLE_NAME;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.patrilang.modele.variable.Variable;

@RequiredArgsConstructor
public class RValueDateVisitor implements RValueVariableVisitor<LocalDate> {
  private final VariableDateDeltaVisitor variableDateDeltaVisitor;

  @Override
  public Variable<LocalDate> apply(VariableContext ctx) {
    var baseDate = visitDate(ctx.date());

    if (isNull(ctx.dateDelta())) {
      return new Variable<>(R_VALUE_VARIABLE_NAME, DATE, baseDate);
    }

    return variableDateDeltaVisitor.apply(baseDate, ctx);
  }

  private static LocalDate visitDate(DateContext ctx) {
    if (nonNull(ctx.MOT_DATE_MINIMUM())) {
      return LocalDate.MIN;
    }

    if (nonNull(ctx.MOT_DATE_INDETERMINER()) || nonNull(ctx.MOT_DATE_MAXIMUM())) {
      return LocalDate.MAX;
    }

    return LocalDate.of(
        parseInt(ctx.annee.getText()), parseInt(ctx.mois.getText()), parseInt(ctx.jour.getText()));
  }
}
