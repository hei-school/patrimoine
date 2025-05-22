package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitMaterielAppreciationFacteur;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class AchatMaterielVisitor
    implements SimpleVisitor<AcheterMaterielContext, AchatMaterielAuComptant> {
  private final VariableVisitor<DateContext, LocalDate> variableDateVisitor;
  private final VariableVisitor<CompteContext, Compte> variableCompteVisitor;

  @Override
  public AchatMaterielAuComptant apply(AcheterMaterielContext ctx) {
    String nom = visitVariableAsText(ctx.variable(0));
    LocalDate t = this.variableDateVisitor.apply(ctx.variable(1));
    Argent valeurComptable = visitVariableAsArgent(ctx.variable(3));
    double tauxDAppreciation = visitVariableAsNombre(ctx.variable(4));
    double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());
    Compte financeur = this.variableCompteVisitor.apply(ctx.variable(5));

    return new AchatMaterielAuComptant(
        nom, t, valeurComptable, tauxDAppreciation / 100 * facteurTauxDAppreciation, financeur);
  }
}
