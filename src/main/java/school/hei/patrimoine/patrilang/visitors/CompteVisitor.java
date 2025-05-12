package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.TextContext;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariable;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;

public class CompteVisitor implements SimplePossessionVisitor<Compte, CompteContext> {
  @Override
  public Compte visit(CompteContext ctx) {
    String nom = visitVariable(ctx.variable(0), TextContext.class, BaseVisitor::visitText);
    Argent valeurComptable =
        visitVariable(ctx.variable(1), ArgentContext.class, BaseVisitor::visitArgent);
    LocalDate t = visitVariable(ctx.variable(2), DateContext.class, BaseVisitor::visitDate);

    return new Compte(nom, t, valeurComptable);
  }
}
