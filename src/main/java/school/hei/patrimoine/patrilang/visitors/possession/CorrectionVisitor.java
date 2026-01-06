package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CorrectionContext;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class CorrectionVisitor implements SimpleVisitor<CorrectionContext, Correction> {
  private final VariableVisitor variableVisitor;
  private final IdVisitor idVisitor;

  @Override
  public Correction apply(CorrectionContext ctx) {
    String id = this.idVisitor.apply(ctx.id());
    Argent valeurComptable = this.variableVisitor.asArgent(ctx.valeurComptable);
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);
    Compte compte = this.variableVisitor.asCompte(ctx.compteNom);

    return new Correction(new FluxArgent(id, compte, t, valeurComptable, null));
  }
}
