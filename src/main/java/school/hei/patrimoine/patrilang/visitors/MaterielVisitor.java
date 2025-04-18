package school.hei.patrimoine.patrilang.visitors;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;

import static java.lang.Integer.parseInt;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.PossedeMaterielContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

public class MaterielVisitor implements PossessionVisitor<Materiel, PossedeMaterielContext> {
    @Override
    public Materiel visit(PossedeMaterielContext ctx) {
        String nom = parseNodeValue(ctx.TEXT());
        LocalDate t = visitDate(ctx.date());
        Argent valeurComptable = visitArgent(ctx.argent());
        double tauxDAppreciation = parseInt(parseNodeValue(ctx.ENTIER()));
        double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.ENTIER());

        return new Materiel(
            nom,
            t,
            t,
            valeurComptable,
            tauxDAppreciation / 100 * facteurTauxDAppreciation
        );
    }
}
