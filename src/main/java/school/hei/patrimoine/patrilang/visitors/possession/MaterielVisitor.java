package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.PossedeMaterielContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class MaterielVisitor implements SimplePossessionVisitor<PossedeMaterielContext, Materiel> {
  private final VariableVisitor<DateContext, LocalDate> variableDateVisitor;

  @Override
  public Materiel apply(PossedeMaterielContext ctx) {
    LocalDate t = this.variableDateVisitor.apply(ctx.variable(1));
    String nom = visitVariableAsText(ctx.variable(2));
    Argent valeurComptable = visitVariableAsArgent(ctx.variable(3));
    double tauxDAppreciation = visitVariableAsNombre(ctx.variable(4));

    double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());

    return new Materiel(
        nom, t, t, valeurComptable, tauxDAppreciation / 100 * facteurTauxDAppreciation);
  }
}
