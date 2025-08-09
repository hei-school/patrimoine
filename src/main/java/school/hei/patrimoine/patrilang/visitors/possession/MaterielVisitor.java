package school.hei.patrimoine.patrilang.visitors.possession;

import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.PossedeMaterielContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.MATERIEL;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class MaterielVisitor implements SimpleVisitor<PossedeMaterielContext, Materiel> {
  private final VariableVisitor variableVisitor;

  @Override
  public Materiel apply(PossedeMaterielContext ctx) {
    String nom = visitText(ctx.materielNom);
    double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());
    double tauxDAppreciation = this.variableVisitor.asNombre(ctx.pourcentageAppreciation);
    Argent valeurComptable = this.variableVisitor.asArgent(ctx.valeurComptable);
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);
    LocalDate dateAcquisition =
        nonNull(ctx.dateObtention) ? this.variableVisitor.asDate(ctx.dateObtention) : t;

    Materiel materiel =
        new Materiel(
            nom,
            dateAcquisition,
            t,
            valeurComptable,
            tauxDAppreciation / 100 * facteurTauxDAppreciation);
    variableVisitor.addToScope(nom, MATERIEL, materiel);
    variableVisitor
        .getVariableScope()
        .parentScope()
        .ifPresent(parent -> parent.add(materiel.nom(), MATERIEL, materiel));
    return materiel;
  }
}
