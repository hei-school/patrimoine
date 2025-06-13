package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CorrectionContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitArgent;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitText;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.visitors.DateVisitor;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class CorrectionVisitor implements SimpleVisitor<CorrectionContext, Correction> {
  private final VariableVisitor variableVisitor;
  private final DateVisitor dateVisitor;

  @Override
  public Correction apply(CorrectionContext ctx) {
    String id = visitText(ctx.id);
    Argent valeurComptable = visitArgent(ctx.valeurComptable);
    LocalDate t = this.dateVisitor.apply(ctx.dateValue);
    Compte compte = this.variableVisitor.asCompte(ctx.compteNom);

    return new Correction(new FluxArgent(id, compte, t, valeurComptable));
  }
}
