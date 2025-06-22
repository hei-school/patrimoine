package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitText;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.patrilang.visitors.DateVisitor;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;

@RequiredArgsConstructor
public class CreanceVisitor implements SimpleVisitor<CompteContext, Creance> {
  private final DateVisitor dateVisitor;
  private final ArgentVisitor argentVisitor;

  @Override
  public Creance apply(CompteContext ctx) {
    String nom = visitText(ctx.nom);
    Argent valeurComptable = this.argentVisitor.apply(ctx.valeurComptable);
    LocalDate t = this.dateVisitor.apply(ctx.dateValue);

    return new Creance(nom, t, valeurComptable);
  }
}
