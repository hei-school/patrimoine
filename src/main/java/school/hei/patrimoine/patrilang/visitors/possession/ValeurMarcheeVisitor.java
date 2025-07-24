package school.hei.patrimoine.patrilang.visitors.possession;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.vente.ValeurMarchee;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.AjoutValeurMarcheeContext;

@RequiredArgsConstructor
public class ValeurMarcheeVisitor implements SimpleVisitor<AjoutValeurMarcheeContext, ValeurMarchee> {
  private final VariableVisitor variableVisitor;

  @Override
  public ValeurMarchee apply(AjoutValeurMarcheeContext ajoutValeurMarcheeContext) {
     throw new UnsupportedOperationException("Not supported yet.");
  }
}
