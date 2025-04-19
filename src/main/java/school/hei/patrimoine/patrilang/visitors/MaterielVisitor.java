package school.hei.patrimoine.patrilang.visitors;

import static java.lang.Integer.parseInt;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.PossedeMaterielContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Materiel;

public class MaterielVisitor implements SimplePossessionVisitor<Materiel, PossedeMaterielContext> {
  @Override
  public Materiel visit(PossedeMaterielContext ctx) {
    String nom = parseNodeValue(ctx.TEXT(1));
    LocalDate t = visitDate(ctx.date());
    Argent valeurComptable = visitArgent(ctx.argent());
    double tauxDAppreciation = parseInt(parseNodeValue(ctx.ENTIER()));
    double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());

    return new Materiel(
        nom, t, t, valeurComptable, tauxDAppreciation / 100 * facteurTauxDAppreciation);
  }
}
