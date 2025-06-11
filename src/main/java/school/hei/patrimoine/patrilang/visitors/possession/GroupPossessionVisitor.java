package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.SousTitreContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitDevise;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitText;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class GroupPossessionVisitor
    implements BiFunction<SousTitreContext, Set<Possession>, GroupePossession> {
  private final VariableVisitor variableVisitor;

  @Override
  public GroupePossession apply(SousTitreContext ctx, Set<Possession> possessions) {
    String nom = visitText(ctx.nom);
    Devise devise = visitDevise(ctx.devise());
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);

    return new GroupePossession(nom, devise, t, possessions);
  }
}
