package school.hei.patrimoine.patrilang.visitors;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Creance;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CreanceContext;

import java.time.LocalDate;

import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

public class CreanceVisitor implements PossessionVisitor<Creance, CreanceContext> {
    @Override
    public Creance visit(CreanceContext ctx) {
        String nom = parseNodeValue(ctx.TEXT());
        LocalDate t = visitDate(ctx.date());
        Argent valeurComptable = visitArgent(ctx.argent());

        return new Creance(nom, t, valeurComptable);
    }
}
