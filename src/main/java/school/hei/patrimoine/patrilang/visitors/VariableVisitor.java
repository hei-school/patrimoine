package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableValueContext;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;
import school.hei.patrimoine.patrilang.modele.Variable;
import school.hei.patrimoine.patrilang.modele.VariableContainer;
import school.hei.patrimoine.patrilang.modele.VariableType;

@SuppressWarnings("all")
@RequiredArgsConstructor
public class VariableVisitor extends PatriLangParserBaseVisitor<Object> {
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
  public Object visitVariable(VariableContext ctx) {
    if (nonNull(ctx.variableValue())) {
      return this.visitVariableValue(ctx.variableValue());
    }

    return this.visitDate(ctx.date());
  }

  @Override
  public Variable<?> visitVariableValue(VariableValueContext ctx) {
    var variableValue = ctx.VARIABLE().getText();
    return this.variableContainer.get(getName(variableValue), getType(variableValue));
  }

  @Override
  public LocalDate visitDate(DateContext ctx) {
    return BaseVisitor.visitDate(ctx);
  }

  private static VariableType getType(String value) {
    return VariableType.fromString(value.substring(0, value.indexOf(COLON)));
  }

  private static String getName(String value) {
    return value.substring(value.indexOf(COLON) + 1);
  }

  private <T> T visitVariableAsExpectedType(Class<?> expectedType, VariableContext ctx) {
    var variable = this.visitVariable(ctx);

    if (!(Variable.class.isInstance(variable))) {
      return (T) variable;
    }

    var castedVariable = (Variable) variable;
    if (!(expectedType.isInstance(castedVariable.value()))) {
      throw new IllegalArgumentException(
          "La variable " + castedVariable.name() + " n'est pas du type " + expectedType);
    }

    return (T) castedVariable.value();
  }
}
