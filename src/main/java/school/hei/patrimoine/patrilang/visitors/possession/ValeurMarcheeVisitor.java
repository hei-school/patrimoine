package school.hei.patrimoine.patrilang.visitors.possession;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.vente.ValeurMarchee;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import java.util.Arrays;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.AjoutValeurMarcheeContext;

@RequiredArgsConstructor
public class ValeurMarcheeVisitor implements SimpleVisitor<AjoutValeurMarcheeContext, ValeurMarchee> {
  private final VariableVisitor variableVisitor;

  @Override
  public ValeurMarchee apply(AjoutValeurMarcheeContext ctx) {
     var possessionAVendre = variableVisitor.asPossession(ctx.possessionAffectee);
     var prixDeVente = this.variableVisitor.asArgent(ctx.prixDeVente);
     var dateDeVente = this.variableVisitor.asDate(ctx.dateDeVente);

     return new ValeurMarchee(possessionAVendre , dateDeVente , prixDeVente);
  }
}
