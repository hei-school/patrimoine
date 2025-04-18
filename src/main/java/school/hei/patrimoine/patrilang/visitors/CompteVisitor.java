package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;

public class CompteVisitor implements PossessionVisitor<Compte, CompteContext> {
  @Override
  public Compte visit(CompteContext ctx) {
    String nom = parseNodeValue(ctx.TEXT());
    LocalDate t = visitDate(ctx.date());
    Argent valeurComptable = visitArgent(ctx.argent());

    return new Compte(nom, t, valeurComptable);
  }
}
