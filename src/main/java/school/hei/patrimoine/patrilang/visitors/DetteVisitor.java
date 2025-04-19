package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Dette;

public class DetteVisitor implements SimplePossessionVisitor<Dette, CompteContext> {
  @Override
  public Dette visit(CompteContext ctx) {
    String nom = parseNodeValue(ctx.TEXT());
    LocalDate t = visitDate(ctx.date());
    Argent valeurComptable = visitArgent(ctx.argent());

    return new Dette(nom, t, valeurComptable.mult(-1));
  }
}
