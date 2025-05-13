package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.PossedeMaterielContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Materiel;

public class MaterielVisitor implements SimplePossessionVisitor<Materiel, PossedeMaterielContext> {
  @Override
  public Materiel visit(PossedeMaterielContext ctx) {
    LocalDate t = visitVariableAsDate(ctx.variable(1));
    String nom = visitVariableAsText(ctx.variable(2));
    Argent valeurComptable = visitVariableAsArgent(ctx.variable(3));
    double tauxDAppreciation = visitVariableAsNombre(ctx.variable(4));

    double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());

    return new Materiel(
        nom, t, t, valeurComptable, tauxDAppreciation / 100 * facteurTauxDAppreciation);
  }
}
