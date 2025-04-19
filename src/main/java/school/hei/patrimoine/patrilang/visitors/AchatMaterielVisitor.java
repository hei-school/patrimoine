package school.hei.patrimoine.patrilang.visitors;

import static java.lang.Integer.parseInt;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.AcheterMaterielContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.modele.PossessionGetter;

public class AchatMaterielVisitor {
  public AchatMaterielAuComptant visit(
      AcheterMaterielContext ctx, PossessionGetter<Compte> compteGetter) {
    String nom = parseNodeValue(ctx.TEXT(1));
    LocalDate t = visitDate(ctx.date());
    Argent valeurComptable = visitArgent(ctx.argent());
    double tauxDAppreciation = parseInt(parseNodeValue(ctx.ENTIER()));
    double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());
    Compte financeur = compteGetter.apply(parseNodeValue(ctx.TEXT(2)));

    return new AchatMaterielAuComptant(
        nom, t, valeurComptable, tauxDAppreciation / 100 * facteurTauxDAppreciation, financeur);
  }
}
