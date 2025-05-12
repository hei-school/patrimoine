package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DeviseContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.SousTitreContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.TextContext;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariable;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Possession;

public class GroupPossessionVisitor {
  public GroupePossession visit(SousTitreContext ctx, Set<Possession> possessions) {
    String nom = visitVariable(ctx.variable(0), TextContext.class, BaseVisitor::visitText);
    LocalDate t = visitVariable(ctx.variable(1), DateContext.class, BaseVisitor::visitDate);
    Devise devise = visitVariable(ctx.variable(2), DeviseContext.class, BaseVisitor::visitDevise);

    return new GroupePossession(nom, devise, t, possessions);
  }
}
