package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.patrilang.modele.VariableContainer;

@Getter
public class VariableVisitor<ContextType extends ParserRuleContext, VariableType>
    implements Function<VariableContext, VariableType> {
  private static final String COLON = ":";
  private final Class<ContextType> contextType;
  private final Function<ContextType, VariableType> visitor;
  private final VariableContainer<VariableType> container;

  public VariableVisitor(
      Class<ContextType> contextType, Function<ContextType, VariableType> visitor) {
    this.contextType = contextType;
    this.visitor = visitor;
    this.container = new VariableContainer<>();
  }

  @Override
  public VariableType apply(VariableContext ctx) {
    return visit(ctx, contextType, this.container, this.visitor);
  }

  public static Argent visitVariableAsArgent(VariableContext ctx) {
    return visit(ctx, ArgentContext.class, notSupportedContainer(), BaseVisitor::visitArgent);
  }

  public static String visitVariableAsText(VariableContext ctx) {
    return visit(ctx, TextContext.class, notSupportedContainer(), BaseVisitor::visitText);
  }

  public static Devise visitVariableAsDevise(VariableContext ctx) {
    return visit(ctx, DeviseContext.class, notSupportedContainer(), BaseVisitor::visitDevise);
  }

  public static double visitVariableAsNombre(VariableContext ctx) {
    return visit(ctx, NombreContext.class, notSupportedContainer(), BaseVisitor::visitNombre);
  }

  private static <T extends ParserRuleContext, R> R visit(
      VariableContext ctx,
      Class<T> expectedCtx,
      VariableContainer<R> container,
      Function<T, R> visitor) {
    var ctxValue = getVariableCtx(ctx);

    if (ctxValue instanceof VariableValueContext) {
      return container.get(parseVariableValue((VariableValueContext) ctxValue));
    }

    if (!expectedCtx.isInstance(ctxValue)) {
      throw new IllegalArgumentException(
          "Variable '"
              + ctxValue.getText()
              + "' does not match expected context: "
              + expectedCtx.getName());
    }

    return visitor.apply(expectedCtx.cast(ctxValue));
  }

  private static ParserRuleContext getVariableCtx(VariableContext ctx) {
    return Stream.of(
            ctx.argent(), ctx.devise(), ctx.date(), ctx.nombre(), ctx.text(), ctx.variableValue())
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }

  private static String parseVariableValue(VariableValueContext ctx) {
    var variableValueAsText = ctx.VARIABLE().getText();

    if (!variableValueAsText.contains(COLON)) {
      throw new IllegalArgumentException("Invalid variabled passed");
    }

    return variableValueAsText.substring(variableValueAsText.indexOf(COLON) + 1);
  }

  private static <T> VariableContainer<T> notSupportedContainer() {
    return new VariableContainer<>();
  }
}
