package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.AcheterMaterielContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariable;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.modele.PossessionGetter;

public class AchatMaterielVisitor {
  public AchatMaterielAuComptant visit(
      AcheterMaterielContext ctx, PossessionGetter<Compte> compteGetter) {
    String nom = visitVariable(ctx.variable(0), TextContext.class, BaseVisitor::visitText);
    LocalDate t = visitVariable(ctx.variable(1), DateContext.class, BaseVisitor::visitDate);
    Argent valeurComptable =
        visitVariable(ctx.variable(3), ArgentContext.class, BaseVisitor::visitArgent);

    double tauxDAppreciation =
        visitVariable(ctx.variable(4), NombreContext.class, BaseVisitor::visitNombre);
    double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());
    Compte financeur =
        compteGetter.apply(
            visitVariable(ctx.variable(6), TextContext.class, BaseVisitor::visitText));

    return new AchatMaterielAuComptant(
        nom, t, valeurComptable, tauxDAppreciation / 100 * facteurTauxDAppreciation, financeur);
  }
}
