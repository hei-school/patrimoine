package school.hei.patrimoine.patrilang.visitors.variable;

import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;
import school.hei.patrimoine.patrilang.modele.variable.Variable;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;
import school.hei.patrimoine.patrilang.modele.variable.VariableType;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;

@Getter
@SuppressWarnings("all")
public class VariableVisitor implements SimpleVisitor<VariableContext, Variable<?>> {
  private static final String COLON = ":";
  public static final String R_VALUE_VARIABLE_NAME = "rvalue_variable";

  private final VariableScope variableScope;
  private final VariableExpressionVisitor variableExpressionVisitor;
  private final VariableArgentVisitor variableArgentVisitor;
  private final VariableDateVisitor variableDateVisitor;

  public VariableVisitor() {
    this(Optional.empty());
  }

  public VariableVisitor(Optional<VariableScope> parentScope) {
    this.variableScope = new VariableScope(parentScope);
    this.variableExpressionVisitor =
        new VariableExpressionVisitor(variableScope, this::getVariableDateVisitor);
    this.variableDateVisitor =
        new VariableDateVisitor(variableScope, this::getVariableExpressionVisitor);
    this.variableArgentVisitor =
        new VariableArgentVisitor(variableScope, variableExpressionVisitor, variableDateVisitor);
  }

  public Cas asCas(VariableContext ctx) {
    return visitVariableAsExpectedType(Cas.class, ctx);
  }

  public Compte asCompte(VariableContext ctx) {
    return visitVariableAsExpectedType(List.of(Compte.class, Creance.class, Dette.class), ctx);
  }

  public Argent asArgent(VariableContext ctx) {
    return visitVariableAsExpectedType(Argent.class, ctx);
  }

  public double asNombre(VariableContext ctx) {
    return visitVariableAsExpectedType(List.of(Double.class, double.class), ctx);
  }

  public int asInt(VariableContext ctx) {
    return (int) this.asNombre(ctx);
  }

  public Dette asDette(VariableContext ctx) {
    return visitVariableAsExpectedType(Dette.class, ctx);
  }

  public Creance asCreance(VariableContext ctx) {
    return visitVariableAsExpectedType(Creance.class, ctx);
  }

  public Possession asPossession(VariableContext ctx) {
    return visitVariableAsExpectedType(Possession.class, ctx);
  }

  public Personne asPersonne(VariableContext ctx) {
    var value = visitVariableAsExpectedType(List.of(Personne.class, PersonneMorale.class), ctx);

    if (Personne.class.isInstance(value)) {
      return (Personne) value;
    }

    return ((PersonneMorale) value).personne();
  }

  public LocalDate asDate(VariableContext ctx) {
    return visitVariableAsExpectedType(LocalDate.class, ctx);
  }

  public <T> void addToScope(String name, VariableType type, T value) {
    this.variableScope.add(name, type, value);
  }

  @Override
  public Variable<?> apply(VariableContext ctx) {
    if (nonNull(ctx.argent())) {
      var value = variableArgentVisitor.apply(ctx.argent());
      return new Variable<>(R_VALUE_VARIABLE_NAME, DATE, value);
    }

    if (nonNull(ctx.date())) {
      var value = variableDateVisitor.apply(ctx.date());
      return new Variable<>(R_VALUE_VARIABLE_NAME, DATE, value);
    }

    if (nonNull(ctx.expression())) {
      var value = variableExpressionVisitor.apply(ctx.expression());
      return new Variable<>(R_VALUE_VARIABLE_NAME, DATE, value);
    }

    var name = extractVariableName(ctx.VARIABLE().getText());
    var type = extractVariableType(ctx.VARIABLE().getText());
    return this.variableScope.get(name, type);
  }

  private <T> T visitVariableAsExpectedType(Class<?> expectedType, VariableContext ctx) {
    return visitVariableAsExpectedType(List.of(expectedType), ctx);
  }

  private <T> T visitVariableAsExpectedType(List<Class<?>> expectedTypes, VariableContext ctx) {
    var variable = (Variable) this.apply(ctx);
    var isExpectedType =
        expectedTypes.stream().anyMatch(expectedType -> expectedType.isInstance(variable.value()));

    if (!isExpectedType) {
      throw new IllegalArgumentException(
          "La variable " + variable.name() + " n'est pas une des types: " + expectedTypes);
    }

    return (T) variable.value();
  }

  public static VariableType extractVariableType(String ctx) {
    return VariableType.fromString(ctx.substring(0, ctx.indexOf(COLON)));
  }

  public static String extractVariableName(String ctx) {
    return ctx.substring(ctx.indexOf(COLON) + 1);
  }
}
