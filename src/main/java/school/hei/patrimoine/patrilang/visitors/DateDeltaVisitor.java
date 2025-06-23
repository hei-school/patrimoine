package school.hei.patrimoine.patrilang.visitors;

import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.function.TriFunction;

@RequiredArgsConstructor
public class DateDeltaVisitor
    implements TriFunction<LocalDate, DateDeltaContext, Boolean, LocalDate> {

  @Override
  public LocalDate apply(LocalDate baseDate, DateDeltaContext ctx, Boolean isMinus) {
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
