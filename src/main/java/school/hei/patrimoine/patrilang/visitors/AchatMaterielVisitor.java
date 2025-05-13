package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.AcheterMaterielContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.modele.PossessionGetter;

public class AchatMaterielVisitor {
  public AchatMaterielAuComptant visit(
      AcheterMaterielContext ctx, PossessionGetter<Compte> compteGetter) {
    String nom = visitVariableAsText(ctx.variable(0));
    LocalDate t = visitVariableAsDate(ctx.variable(1));
    Argent valeurComptable = visitVariableAsArgent(ctx.variable(3));
    double tauxDAppreciation = visitVariableAsNombre(ctx.variable(4));
    double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());
    Compte financeur = compteGetter.apply(visitVariableAsText(ctx.variable(6)));

    return new AchatMaterielAuComptant(
        nom, t, valeurComptable, tauxDAppreciation / 100 * facteurTauxDAppreciation, financeur);
  }
}
