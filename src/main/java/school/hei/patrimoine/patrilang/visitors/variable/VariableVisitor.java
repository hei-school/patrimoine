package school.hei.patrimoine.patrilang.visitors.variable;

import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.PersonneMorale;
import school.hei.patrimoine.patrilang.modele.variable.Variable;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;
import school.hei.patrimoine.patrilang.modele.variable.VariableType;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;

@Getter
@SuppressWarnings("all")
public class VariableVisitor implements SimpleVisitor<VariableContext, Variable<?>> {
  private static final String COLON = ":";
  public static final String R_VALUE_VARIABLE_NAME = "rvalue_variable";
  private static final VariableDateDeltaVisitor DATE_DELTA_VISITOR = new VariableDateDeltaVisitor();

  private final VariableScope variableScope;
  private final RValueNombreVisitor rValueNombreVisitor = new RValueNombreVisitor();
  private final RValueDateVisitor rValueDateVisitor = new RValueDateVisitor(DATE_DELTA_VISITOR);

  public VariableVisitor() {
    this.variableScope = new VariableScope(Optional.empty());
  }

  public VariableVisitor(Optional<VariableScope> parentScope) {
    this.variableScope = new VariableScope(parentScope);
  }

  public Compte asCompte(VariableContext ctx) {
    return visitVariableAsExpectedType(List.of(Compte.class, Creance.class, Dette.class), ctx);
  }

  public double asNombre(VariableContext ctx) {
    return visitVariableAsExpectedType(List.of(Double.class, double.class), ctx);
  }

  public int asInt(VariableContext ctx) {
    double value = visitVariableAsExpectedType(List.of(Double.class, double.class), ctx);
    return (int) value;
  }

  public Dette asDette(VariableContext ctx) {
    return visitVariableAsExpectedType(Dette.class, ctx);
  }

  public Creance asCreance(VariableContext ctx) {
    return visitVariableAsExpectedType(Creance.class, ctx);
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
    if (nonNull(ctx.nombre())) {
      return rValueNombreVisitor.apply(ctx, this);
    }

    if (nonNull(ctx.date())) {
      return rValueDateVisitor.apply(ctx, this);
    }

    var baseValue = this.variableScope.get(extractVariableName(ctx), extractVariableType(ctx));

    if (nonNull(ctx.dateDelta())) {
      assertVariableType(baseValue, DATE);
      return DATE_DELTA_VISITOR.apply((LocalDate) baseValue.value(), ctx, this);
    }

    return baseValue;
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

  private void assertVariableType(Variable<?> value, VariableType expectedType) {
    if (!expectedType.equals(value.type())) {
      throw new IllegalArgumentException(
          "Le type attendu est : "
              + expectedType
              + ", mais le type trouvé est : "
              + value.type()
              + ".");
    }
  }
}
