package school.hei.patrimoine.patrilang.visitors.possession.pj;

import lombok.RequiredArgsConstructor;

import school.hei.patrimoine.patrilang.antlr.PatriLangParser.PieceJustificativeContext;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class PjVisitor {
    private final VariableVisitor variableVisitor;
    private final IdVisitor idVisitor;

    public String apply(PieceJustificativeContext ctx) {
        String idFlux = this.idVisitor.apply(ctx.id());
        String dateEmission = this.variableVisitor.asDate(ctx.dateValue).toString();
        String driveLink = ctx.pjUrl.getText();

        return String.format("* `%s`, %s, %s", idFlux, dateEmission, driveLink);
    }
}
