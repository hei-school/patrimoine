package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitArgent;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitText;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class CreanceVisitor implements SimpleVisitor<CompteContext, Creance> {
  private final VariableVisitor variableDateVisitor;

  @Override
  public Creance apply(CompteContext ctx) {
    String nom = visitText(ctx.nom);
    Argent valeurComptable = visitArgent(ctx.valeurComptable);
    LocalDate t = this.variableDateVisitor.asDate(ctx.dateValue);

    return new Creance(nom, t, valeurComptable);
  }
}
