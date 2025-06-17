package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitText;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.patrilang.visitors.DateVisitor;

@RequiredArgsConstructor
public class DetteVisitor implements SimpleVisitor<CompteContext, Dette> {
  private final DateVisitor dateVisitor;
  private final ArgentVisitor argentVisitor;

  @Override
  public Dette apply(CompteContext ctx) {
    String nom = visitText(ctx.nom);
    Argent valeurComptable = this.argentVisitor.apply(ctx.valeurComptable);
    LocalDate t = this.dateVisitor.apply(ctx.dateValue);

    return new Dette(nom, t, valeurComptable.mult(-1));
  }
}
