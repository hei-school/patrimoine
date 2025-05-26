package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.VariableVisitorType.AUTO_SAVE;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.VariableVisitorType.MANUAL_SAVE;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.antlr.v4.runtime.ParserRuleContext;
import school.hei.patrimoine.Pair;
import school.hei.patrimoine.patrilang.modele.VariableContainer;

public class VariableVisitor<ContextType extends ParserRuleContext, VariableType> {
  private final Class<ContextType> contextType;
  private final Function<ContextType, VariableType> visitor;
  private final Function<VariableType, String> getName;
  private final VariableContainer<VariableType> container;
  private final VariableVisitorType visitorType;
  private static final String COLON = ":";

  public VariableVisitor(
      Class<ContextType> contextType,
      Function<ContextType, VariableType> visitor,
      Function<VariableType, String> getName) {
    this(contextType, visitor, getName, AUTO_SAVE);
  }

  public VariableVisitor(
      Class<ContextType> contextType, Function<ContextType, VariableType> visitor) {
    this(contextType, visitor, defaultGetName(), MANUAL_SAVE);
  }

  public VariableVisitor(
      Class<ContextType> contextType,
      Function<ContextType, VariableType> visitor,
      Function<VariableType, String> getName,
      VariableVisitorType visitorType) {
    this.contextType = contextType;
    this.visitor = visitor;
    this.getName = getName;
    this.visitorType = visitorType;
    this.container = new VariableContainer<>();
  }

  public VariableType apply(VariableContext ctx) {
    return visit(ctx, this);
  }

  public VariableType apply(ContextType ctx) {
    var newVariable = this.visitor.apply(ctx);

    if (AUTO_SAVE.equals(this.visitorType)) {
      this.save(newVariable);
    }

    return newVariable;
  }

  public void save(Pair<String, VariableType> value) {
    this.container.add(value);
  }

  public void save(VariableType value) {
    this.container.add(new Pair<>(getName.apply(value), value));
  }

  public static String visitVariableAsText(VariableContext ctx) {
    return visit(
        ctx, new VariableVisitor<>(TextContext.class, BaseVisitor::visitText, defaultGetName()));
  }

  private static <T extends ParserRuleContext, R> R visit(
      VariableContext ctx, VariableVisitor<T, R> variableVisitor) {
    var ctxValue = getVariableCtx(ctx).orElseThrow();

    if (ctxValue instanceof VariableValueContext) {
      return variableVisitor.container.get(parseVariableValue((VariableValueContext) ctxValue));
    }

    if (!variableVisitor.contextType.isInstance(ctxValue)) {
      throw new IllegalArgumentException(
          "La variable '"
              + ctxValue.getText()
              + "' ne correspond pas au contexte attendu : "
              + variableVisitor.contextType.getName());
    }

    return variableVisitor.apply(variableVisitor.contextType.cast(ctxValue));
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

  private static <T> Function<T, String> defaultGetName() {
    return Objects::toString;
  }

  /** Indicates how variables are persisted during visiting. */
  public enum VariableVisitorType {
    /** Automatically saves visited variables. */
    AUTO_SAVE,

    /** Requires manual saving of visited variables. */
    MANUAL_SAVE
  }
}
