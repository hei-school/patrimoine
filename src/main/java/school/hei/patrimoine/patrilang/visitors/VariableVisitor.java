package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableContext;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.patrilang.modele.Variable;
import school.hei.patrimoine.patrilang.modele.VariableContainer;
import school.hei.patrimoine.patrilang.modele.VariableType;
import school.hei.patrimoine.patrilang.visitors.possession.SimpleVisitor;

@SuppressWarnings("all")
@RequiredArgsConstructor
public class VariableVisitor implements SimpleVisitor<VariableContext, Variable<?>> {
  private static final String COLON = ":";
  private final VariableContainer variableContainer;

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

  @Override
  public Variable<?> apply(VariableContext ctx) {
    var variableValue = ctx.VARIABLE().getText();
    return this.variableContainer.get(getName(variableValue), getType(variableValue));
  }

  private static VariableType getType(String value) {
    return VariableType.fromString(value.substring(0, value.indexOf(COLON)));
  }

  private static String getName(String value) {
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
