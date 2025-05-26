package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import org.antlr.v4.runtime.ParserRuleContext;
import school.hei.patrimoine.Pair;
import school.hei.patrimoine.patrilang.modele.VariableContainer;

public class VariableVisitor<ContextType extends ParserRuleContext, VariableType> {
  private final Class<ContextType> contextType;
  private final Function<ContextType, VariableType> visitor;
  private final VariableContainer<VariableType> container;
  private static final String COLON = ":";

  public VariableVisitor(
      Class<ContextType> contextType, Function<ContextType, VariableType> visitor) {
    this.contextType = contextType;
    this.visitor = visitor;
    this.container = new VariableContainer<>();
  }

  public VariableType apply(VariableContext ctx) {
    return visit(ctx, contextType, this.container, this.visitor);
  }

  public VariableType apply(ContextType ctx) {
    return this.visitor.apply(ctx);
  }

  public void storeVariables(Set<Pair<String, VariableType>> variables) {
    this.container.addAll(variables);
  }

  public static String visitVariableAsText(VariableContext ctx) {
    return visit(ctx, TextContext.class, new VariableContainer<>(), BaseVisitor::visitText);
  }

  private static <T extends ParserRuleContext, R> R visit(
      VariableContext ctx,
      Class<T> expectedCtx,
      VariableContainer<R> container,
      Function<T, R> visitor) {
    var ctxValue = getVariableCtx(ctx).orElseThrow();

    if (ctxValue instanceof VariableValueContext) {
      return container.get(parseVariableValue((VariableValueContext) ctxValue));
    }

    if (!expectedCtx.isInstance(ctxValue)) {
      throw new IllegalArgumentException(
          "La variable '"
              + ctxValue.getText()
              + "' ne correspond pas au contexte attendu : "
              + expectedCtx.getName());
    }

    return visitor.apply(expectedCtx.cast(ctxValue));
  }

  private static Optional<ParserRuleContext> getVariableCtx(VariableContext ctx) {
    return Stream.of(ctx.date(), ctx.text(), ctx.variableValue())
        .filter(Objects::nonNull)
        .findFirst();
  }

  private static String parseVariableValue(VariableValueContext ctx) {
    var variableValueAsText = ctx.VARIABLE().getText();

    if (!variableValueAsText.contains(COLON)) {
      throw new IllegalArgumentException(
          "Variable invalide : '" + variableValueAsText + "'. Format attendu : <type>:<nom>");
    }

    return variableValueAsText.substring(variableValueAsText.indexOf(COLON) + 1);
  }
}
