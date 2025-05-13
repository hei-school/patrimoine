package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import org.antlr.v4.runtime.ParserRuleContext;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;

public class VariableVisitor {
  public static Argent visitVariableAsArgent(VariableContext ctx) {
    return visit(ctx, ArgentContext.class, BaseVisitor::visitArgent);
  }

  public static LocalDate visitVariableAsDate(VariableContext ctx) {
    return visit(ctx, DateContext.class, BaseVisitor::visitDate);
  }

  public static String visitVariableAsText(VariableContext ctx) {
    return visit(ctx, TextContext.class, BaseVisitor::visitText);
  }

  public static Devise visitVariableAsDevise(VariableContext ctx) {
    return visit(ctx, DeviseContext.class, BaseVisitor::visitDevise);
  }

  public static double visitVariableAsNombre(VariableContext ctx) {
    return visit(ctx, NombreContext.class, BaseVisitor::visitNombre);
  }

  public static <T extends ParserRuleContext, R> R visit(
      VariableContext ctx, Class<T> expectedCtx, Function<T, R> getter) {
    var ctxValue = getVariableCtx(ctx);

    if (!expectedCtx.isInstance(ctxValue)) {
      throw new IllegalArgumentException(
          "Variable '"
              + ctxValue.getText()
              + "' does not match expected context: "
              + expectedCtx.getName());
    }

    return getter.apply(expectedCtx.cast(ctxValue));
  }

  private static ParserRuleContext getVariableCtx(VariableContext ctx) {
    return Stream.of(
            ctx.argent(), ctx.devise(), ctx.date(), ctx.nombre(), ctx.text(), ctx.variableValue())
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }
}
