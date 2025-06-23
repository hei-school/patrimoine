package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateDeltaContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitDate;

import java.time.LocalDate;
import java.util.Optional;
import lombok.Getter;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.patrilang.modele.variable.Variable;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;
import school.hei.patrimoine.patrilang.modele.variable.VariableType;

@Getter
@SuppressWarnings("all")
public class VariableVisitor implements SimpleVisitor<VariableContext, Variable<?>> {
  private static final String COLON = ":";
  private final VariableScope variableScope;
  private static final String R_VALUE_VARIABLE_NAME = "rvalue_variable";
  private static final DateDeltaVisitor dateDeltaVisitor = new DateDeltaVisitor();

  public VariableVisitor() {
    this.variableScope = new VariableScope(Optional.empty());
  }

  public VariableVisitor(Optional<VariableScope> parentScope) {
    this.variableScope = new VariableScope(parentScope);
  }

  public Compte asCompte(VariableContext ctx) {
    return visitVariableAsExpectedType(Compte.class, ctx);
  }

  public Dette asDette(VariableContext ctx) {
    return visitVariableAsExpectedType(Dette.class, ctx);
  }

  public Creance asCreance(VariableContext ctx) {
    return visitVariableAsExpectedType(Creance.class, ctx);
  }

  public Personne asPersonne(VariableContext ctx) {
    return visitVariableAsExpectedType(Personne.class, ctx);
  }

  public LocalDate asDate(VariableContext ctx) {
    return visitVariableAsExpectedType(LocalDate.class, ctx);
  }

  public <T> void addToScope(String name, VariableType type, T value) {
    this.variableScope.add(name, type, value);
  }

  @Override
  public Variable<?> apply(VariableContext ctx) {
    if (nonNull(ctx.date())) {
      return new Variable(R_VALUE_VARIABLE_NAME, DATE, visitDate(ctx.date()));
    }

    var isMinus = nonNull(ctx.MOINS());
    var baseValue = this.variableScope.get(extractVariableName(ctx), extractVariableType(ctx));

    if (nonNull(ctx.dateDelta())) {
      return visitDateDelta(baseValue, ctx.dateDelta(), isMinus);
    }

    return baseValue;
  }

  private static Variable<LocalDate> visitDateDelta(
      Variable<?> baseValue, DateDeltaContext ctx, boolean isMinus) {
    if (!DATE.equals(baseValue.type())) {
      throw new IllegalArgumentException(
          "La variable "
              + baseValue.name()
              + " doit être de type DATE pour utiliser les opérations comme + y années.");
    }

    var newDate = dateDeltaVisitor.apply((LocalDate) baseValue.value(), ctx, isMinus);

    return new Variable(R_VALUE_VARIABLE_NAME, DATE, newDate);
  }

  public static VariableType extractVariableType(VariableContext ctx) {
    var value = ctx.VARIABLE().getText();
    return VariableType.fromString(value.substring(0, value.indexOf(COLON)));
  }

  public static String extractVariableName(VariableContext ctx) {
    var value = ctx.VARIABLE().getText();
    return value.substring(value.indexOf(COLON) + 1);
  }

  private <T> T visitVariableAsExpectedType(Class<?> expectedType, VariableContext ctx) {
    var variable = (Variable) this.apply(ctx);

    if (!(expectedType.isInstance(variable.value()))) {
      throw new IllegalArgumentException(
          "La variable " + variable.name() + " n'est pas du type " + expectedType);
    }

    return (T) variable.value();
  }
}
