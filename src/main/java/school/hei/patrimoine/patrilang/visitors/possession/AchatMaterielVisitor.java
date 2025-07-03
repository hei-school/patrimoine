package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class AchatMaterielVisitor
    implements SimpleVisitor<AcheterMaterielContext, AchatMaterielAuComptant> {
  private final VariableVisitor variableVisitor;

  @Override
  public AchatMaterielAuComptant apply(AcheterMaterielContext ctx) {
    String materielNom = visitText(ctx.materielNom);
    double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());
    double tauxDAppreciation = this.variableVisitor.asNombre(ctx.pourcentageAppreciation);
    Argent valeurComptable = this.variableVisitor.asArgent(ctx.valeurComptable);
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);
    Compte financeur = this.variableVisitor.asCompte(ctx.compteDebiteurNom);

    return new AchatMaterielAuComptant(
        materielNom,
        t,
        valeurComptable,
        tauxDAppreciation / 100 * facteurTauxDAppreciation,
        financeur);
  }
}
