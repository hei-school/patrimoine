package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import org.antlr.v4.runtime.ParserRuleContext;

public class VariableVisitor {
  public static <T extends ParserRuleContext, R> R visitVariable(
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
