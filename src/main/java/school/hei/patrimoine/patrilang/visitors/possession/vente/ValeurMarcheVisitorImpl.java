package school.hei.patrimoine.patrilang.visitors.possession;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.vente.ValeurMarche;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import java.time.LocalDate;
import java.util.Map;

public class ValeurMarcheVisitorImpl implements ValeurMarcheVisitor {
    private final Map<String, Possession> possessions;
    private final VariableVisitor variableVisitor;

    public ValeurMarcheVisitorImpl(
            Map<String, Possession> possessions,
            VariableVisitor variableVisitor) {
        this.possessions = possessions;
        this.variableVisitor = variableVisitor;
    }

    @Override
    public void visit(PatriLangParser.DeclarationValeurMarcheContext ctx) {
        String nomPossession = ctx.possession.getText();
        Possession possession = possessions.get(nomPossession);

        if (possession == null) {
            throw new IllegalArgumentException("Possession inconnue: " + nomPossession);
        }

        LocalDate date = variableVisitor.asDate(ctx.dateEvaluation);
        Argent valeur = variableVisitor.asArgent(ctx.variable());

        new ValeurMarche(possession, date, valeur);
    }
}
