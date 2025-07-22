package school.hei.patrimoine.patrilang.visitors.possession;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.ValeurMarche.ValeurMarche;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.ValeurMarcheDeclarationContext;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class ValeurMarcheVisitor implements SimpleVisitor<ValeurMarcheDeclarationContext, ValeurMarche> {
    private final VariableVisitor variableVisitor;

    @Override
    public ValeurMarche apply(PatriLangParser.ValeurMarcheDeclarationContext ctx) {
        Possession possession = variableVisitor.asPossession(ctx.possessionNom);
        LocalDate t = this.variableVisitor.asDate(ctx.dateValue);
        Argent valeurMarche = this.variableVisitor.asArgent(ctx.valeurMarche);

        return new ValeurMarche(possession, t, valeurMarche);
    }

}
