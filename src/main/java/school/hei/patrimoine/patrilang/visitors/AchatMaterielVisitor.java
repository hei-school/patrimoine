package school.hei.patrimoine.patrilang.visitors;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Compte;

import java.time.LocalDate;
import java.util.function.Function;

import static java.lang.Integer.parseInt;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.AcheterMaterielContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

@RequiredArgsConstructor
public class AchatMaterielVisitor implements PossessionVisitor<AchatMaterielAuComptant, AcheterMaterielContext> {
    private final Function<String, Compte> compteGetter;

    @Override
    public AchatMaterielAuComptant visit(AcheterMaterielContext ctx) {
        String nom = parseNodeValue(ctx.TEXT(0));
        LocalDate t = visitDate(ctx.date());
        Argent valeurComptable = visitArgent(ctx.argent());
        double tauxDAppreciation = parseInt(parseNodeValue(ctx.ENTIER()));
        double facteurTauxDAppreciation = visitMaterielAppreciationFacteur(ctx.ENTIER());
        Compte financeur = compteGetter.apply(parseNodeValue(ctx.TEXT(1)));

        return new AchatMaterielAuComptant(
            nom,
            t,
            valeurComptable,
            tauxDAppreciation / 100 * facteurTauxDAppreciation,
            financeur
        );
    }
}
