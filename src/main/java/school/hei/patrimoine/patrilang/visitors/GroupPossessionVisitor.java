package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.SousTitreContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Possession;

public class GroupPossessionVisitor {
  public GroupePossession visit(SousTitreContext ctx, Set<Possession> possessions) {
    String nom = parseNodeValue(ctx.TEXT());
    LocalDate t = visitDate(ctx.date());
    Devise devise = visitDevise(ctx.DEVISE());

    return new GroupePossession(nom, devise, t, possessions);
  }
}
