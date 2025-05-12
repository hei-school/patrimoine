package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.NombreContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.PossedeMaterielContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.TextContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariable;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Materiel;

public class MaterielVisitor implements SimplePossessionVisitor<Materiel, PossedeMaterielContext> {
  @Override
  public Materiel visit(PossedeMaterielContext ctx) {
    LocalDate t = visitVariable(ctx.variable(1), DateContext.class, BaseVisitor::visitDate);
    String nom = visitVariable(ctx.variable(2), TextContext.class, BaseVisitor::visitText);
    Argent valeurComptable =
        visitVariable(ctx.variable(3), ArgentContext.class, BaseVisitor::visitArgent);
    double tauxDAppreciation =
        visitVariable(ctx.variable(4), NombreContext.class, BaseVisitor::visitNombre);

    double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());

    return new Materiel(
        nom, t, t, valeurComptable, tauxDAppreciation / 100 * facteurTauxDAppreciation);
  }
}
