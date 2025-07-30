package school.hei.patrimoine.patrilang.visitors.possession;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.vente.ValeurMarche;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.ValeurMarcheContext;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class ValeurMarcheVisitor implements SimpleVisitor<ValeurMarcheContext, ValeurMarche> {
  private final VariableVisitor variableVisitor;

  @Override
  public ValeurMarche apply(ValeurMarcheContext ctx) {
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);
    Possession possession = this.variableVisitor.asPossession(ctx.possession);
    Argent valeur = this.variableVisitor.asArgent(ctx.valeur);
    return new ValeurMarche(possession, t, valeur);
  }
}
