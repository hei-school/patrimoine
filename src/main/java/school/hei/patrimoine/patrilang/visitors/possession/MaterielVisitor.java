package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.PossedeMaterielContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.patrilang.visitors.DateVisitor;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;

@RequiredArgsConstructor
public class MaterielVisitor implements SimpleVisitor<PossedeMaterielContext, Materiel> {
  private final DateVisitor dateVisitor;
  private final ArgentVisitor argentVisitor;

  @Override
  public Materiel apply(PossedeMaterielContext ctx) {
    String nom = visitText(ctx.materielNom);
    double tauxDAppreciation = visitNombre(ctx.pourcentageAppreciation);
    double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());
    Argent valeurComptable = this.argentVisitor.apply(ctx.valeurComptable);
    LocalDate t = this.dateVisitor.apply(ctx.dateValue);

    return new Materiel(
        nom, t, t, valeurComptable, tauxDAppreciation / 100 * facteurTauxDAppreciation);
  }
}
