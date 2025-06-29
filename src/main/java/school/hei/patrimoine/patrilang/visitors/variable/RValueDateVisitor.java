package school.hei.patrimoine.patrilang.visitors.variable;

import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.mapper.MonthTokenMapper.stringToMonth;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.R_VALUE_VARIABLE_NAME;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;
import school.hei.patrimoine.patrilang.modele.variable.Variable;

@RequiredArgsConstructor
public class RValueDateVisitor extends PatriLangParserBaseVisitor<LocalDate> implements RValueVariableVisitor<LocalDate> {
  private final VariableDateDeltaVisitor variableDateDeltaVisitor;

  @Override
  public Variable<LocalDate> apply(VariableContext ctx) {
    var baseDate = this.visit(ctx.date());

    if (isNull(ctx.dateDelta())) {
      return new Variable<>(R_VALUE_VARIABLE_NAME, DATE, baseDate);
    }

    return variableDateDeltaVisitor.apply(baseDate, ctx);
  }

  @Override
  public LocalDate visitDateMaximum(DateMaximumContext ctx) {
    return LocalDate.MAX;
  }

  @Override
  public LocalDate visitDateMinimum(DateMinimumContext ctx) {
    return LocalDate.MIN;
  }

  @Override
  public LocalDate visitDateIndeterminee(DateIndetermineeContext ctx) {
    return LocalDate.MAX;
  }

  @Override
  public LocalDate visitDateEntier(DateEntierContext ctx) {
    return LocalDate.of(
            parseInt(ctx.annee.getText()), parseInt(ctx.mois.getText()), parseInt(ctx.jour.getText()));
  }

  @Override
  public LocalDate visitDateTextuelle(DateTextuelleContext ctx) {
    return LocalDate.of(
        parseInt(ctx.annee.getText()),
        stringToMonth(ctx.mois.getText()),
        parseInt(ctx.jour.getText())
    );
  }
}
