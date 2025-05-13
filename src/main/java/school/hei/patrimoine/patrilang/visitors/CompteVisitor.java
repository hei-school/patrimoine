package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;

public class CompteVisitor implements SimplePossessionVisitor<Compte, CompteContext> {
  @Override
  public Compte visit(CompteContext ctx) {
    String nom = visitVariableAsText(ctx.variable(0));
    Argent valeurComptable = visitVariableAsArgent(ctx.variable(1));
    LocalDate t = visitVariableAsDate(ctx.variable(2));

    return new Compte(nom, t, valeurComptable);
  }
}
