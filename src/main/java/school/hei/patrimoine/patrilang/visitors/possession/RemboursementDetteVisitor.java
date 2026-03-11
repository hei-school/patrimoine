package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.RembourserDetteContext;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.RemboursementDette;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class RemboursementDetteVisitor
    implements SimpleVisitor<RembourserDetteContext, RemboursementDette> {
  private final VariableVisitor variableVisitor;
  private final IdVisitor idVisitor;

  @Override
  public RemboursementDette apply(RembourserDetteContext ctx) {
    var id = this.idVisitor.apply(ctx.id());
    var rembourseur = this.variableVisitor.asCompte(ctx.rembourseur);
    var rembourser = this.variableVisitor.asCompte(ctx.rembourse);
    var creance = this.variableVisitor.asCreance(ctx.creance);
    var dette = this.variableVisitor.asDette(ctx.dette);
    var date = this.variableVisitor.asDate(ctx.dateValue);
    var valeurComptable = this.variableVisitor.asArgent(ctx.valeurComptable);

    return new RemboursementDette(
        id, rembourseur, rembourser, dette, creance, date, valeurComptable);
  }
}
