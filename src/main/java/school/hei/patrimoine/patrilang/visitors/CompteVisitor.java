package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;

public class CompteVisitor {
  public static Compte visitCompte(PatriLangParser.CompteContext ctx) {
    String nom = parseNodeValue(ctx.TEXT());
    LocalDate t = visitDate(ctx.date());
    Argent valeurComptable = visitArgent(ctx.argent());

    return new Compte(nom, t, valeurComptable);
  }
}
