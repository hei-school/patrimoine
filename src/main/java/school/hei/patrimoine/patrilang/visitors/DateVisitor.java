package school.hei.patrimoine.patrilang.visitors;

import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.patrilang.visitors.possession.SimpleVisitor;

@RequiredArgsConstructor
public class DateVisitor implements SimpleVisitor<DateContext, LocalDate> {
  private final VariableVisitor variableVisitor;

  @Override
  public LocalDate apply(DateContext ctx) {
    if (nonNull(ctx.MOT_DATE_MINIMUM())) {
      return LocalDate.MIN;
    }

    if (nonNull(ctx.MOT_DATE_INDETERMINER()) || nonNull(ctx.MOT_DATE_MAXIMUM())) {
      return LocalDate.MAX;
    }

    if (nonNull(ctx.dateExpr())) {
      return visitDateExpr(ctx.dateExpr());
    }

    return LocalDate.of(
        parseInt(ctx.annee.getText()), parseInt(ctx.mois.getText()), parseInt(ctx.jour.getText()));
  }

  private LocalDate visitDateExpr(DateExprContext ctx) {
    var baseDate = this.variableVisitor.asDate(ctx.variable());
    var isMinus = nonNull(ctx.MOINS());

    if (isNull(ctx.dateDelta())) {
      return baseDate;
    }

    return applyDelta(isMinus, baseDate, ctx.dateDelta());
  }

  private LocalDate applyDelta(boolean isMinus, LocalDate baseDate, DateDeltaContext ctx) {
    var anneePart = visitAnneePart(ctx.anneePart());
    var moisPart = visitMoisPart(ctx.moisPart());
    var joursPart = visitJours(ctx.jourPart());

    if (isMinus) {
      return baseDate.minusYears(anneePart).minusMonths(moisPart).minusDays(joursPart);
    }

    return baseDate.plusYears(anneePart).plusMonths(moisPart).plusDays(joursPart);
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
