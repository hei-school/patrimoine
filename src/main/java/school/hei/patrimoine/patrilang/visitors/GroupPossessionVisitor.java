package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.SousTitreContext;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Possession;

public class GroupPossessionVisitor {
  public GroupePossession visit(SousTitreContext ctx, Set<Possession> possessions) {
    String nom = visitVariableAsText(ctx.variable(0));
    LocalDate t = visitVariableAsDate(ctx.variable(1));
    Devise devise = visitVariableAsDevise(ctx.variable(2));

    return new GroupePossession(nom, devise, t, possessions);
  }
}
