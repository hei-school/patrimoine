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
      var possessionType = Arrays.stream(ctx.possessionAffectee.VARIABLE().toString().split(":")).toList().getFirst();
      var possessionAVendre = switch (possessionType){
        case  "Trésoreries" -> variableVisitor.asCompte(ctx.possessionAffectee);
        case "Créances" -> variableVisitor.asCompte(ctx.possessionAffectee);
        case "Dettes" -> variableVisitor.asCompte(ctx.possessionAffectee);
        default -> throw new IllegalStateException("Une erreur est survenue !" + possessionType);
      };
     var prixDeVente = this.variableVisitor.asArgent(ctx.prixDeVente);
     var dateDeVente = this.variableVisitor.asDate(ctx.dateDeVente);

     return new ValeurMarchee(possessionAVendre , dateDeVente , prixDeVente);
  }
}
