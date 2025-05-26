package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsText;

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
    String materielNom = visitVariableAsText(ctx.materielNom);
    Argent valeurComptable = visitArgent(ctx.valeurComptable);
    double tauxDAppreciation = visitNombre(ctx.pourcentageAppreciation);
    double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());
    LocalDate t = this.variableDateVisitor.apply(ctx.dateValue);
    Compte financeur = this.variableCompteVisitor.apply(ctx.compteDebiteurNom);

    return new AchatMaterielAuComptant(
        materielNom,
        t,
        valeurComptable,
        tauxDAppreciation / 100 * facteurTauxDAppreciation,
        financeur);
  }
}
