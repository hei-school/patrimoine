package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CorrectionContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitCorrectionQualificateurFacteur;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsArgent;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsText;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class CorrectionVisitor implements SimpleVisitor<CorrectionContext, Correction> {
  private final VariableVisitor<DateContext, LocalDate> variableDateVisitor;
  private final VariableVisitor<CompteContext, Compte> variableCompteVisitor;

  @Override
  public Correction apply(CorrectionContext ctx) {
    String id = visitVariableAsText(ctx.variable(0));
    LocalDate t = this.variableDateVisitor.apply(ctx.variable(1));
    Argent valeurComptable = visitVariableAsArgent(ctx.variable(2));
    Compte compte = this.variableCompteVisitor.apply(ctx.variable(3));
    double facteur = visitCorrectionQualificateurFacteur(ctx.MOT_CORRECTION_QUALIFICATEUR());

    return new Correction(new FluxArgent(id, compte, t, valeurComptable.mult(facteur)));
  }
}
