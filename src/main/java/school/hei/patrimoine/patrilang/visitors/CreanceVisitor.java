package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateContext;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Creance;

@RequiredArgsConstructor
public class CreanceVisitor implements SimplePossessionVisitor<CompteContext, Creance> {
  private final VariableVisitor<DateContext, LocalDate> variableDateVisitor;

  @Override
  public Creance apply(CompteContext ctx) {
    String nom = visitVariableAsText(ctx.variable(0));
    Argent valeurComptable = visitVariableAsArgent(ctx.variable(1));
    LocalDate t = this.variableDateVisitor.apply(ctx.variable(2));

    return new Creance(nom, t, valeurComptable);
  }
}
