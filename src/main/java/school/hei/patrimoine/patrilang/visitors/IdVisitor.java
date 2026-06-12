package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static school.hei.patrimoine.modele.normalizer.PossessionNomNormalizer.normalize;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.IdContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.NOMBRE;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitText;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@Slf4j
@RequiredArgsConstructor
public class IdVisitor implements SimpleVisitor<IdContext, String> {
  private final VariableVisitor variableVisitor;

  @Override
  public String apply(IdContext ctx) {
    var baseId = visitText(ctx.text());

    if (isNull(ctx.variable())) {
      return baseId;
    }

    var variable = this.variableVisitor.apply(ctx.variable());
    var variableValue = variable.value();

    if (NOMBRE.equals(variable.type())) {
      if (variableValue instanceof Number numberValue) {
        variableValue = numberValue.intValue();
      }
    }

    return normalize(baseId + variableValue);
  }
}
