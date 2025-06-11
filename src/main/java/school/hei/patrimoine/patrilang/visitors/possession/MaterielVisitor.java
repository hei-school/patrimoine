package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.PossedeMaterielContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class MaterielVisitor implements SimpleVisitor<PossedeMaterielContext, Materiel> {
  private final VariableVisitor variableVisitor;

  @Override
  public Materiel apply(PossedeMaterielContext ctx) {
    String nom = visitText(ctx.materielNom);
    Argent valeurComptable = visitArgent(ctx.valeurComptable);
    double tauxDAppreciation = visitNombre(ctx.pourcentageAppreciation);
    double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);

    return new Materiel(
        nom, t, t, valeurComptable, tauxDAppreciation / 100 * facteurTauxDAppreciation);
  }
}
