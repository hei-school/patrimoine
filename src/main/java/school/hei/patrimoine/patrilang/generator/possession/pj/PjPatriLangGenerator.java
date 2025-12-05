package school.hei.patrimoine.patrilang.generator.possession.piecejustificative;

import school.hei.patrimoine.modele.possession.pj.PiecesJustificative;
import school.hei.patrimoine.patrilang.generator.DatePatriLangGenerator;
import school.hei.patrimoine.patrilang.generator.IdPatriLangGenerator;
import school.hei.patrimoine.patrilang.generator.PatriLangGenerator;

public class PjPatriLangGenerator implements PatriLangGenerator<PiecesJustificative> {
    private final IdPatriLangGenerator idGenerator;
    private final DatePatriLangGenerator dateGenerator;

    public PjPatriLangGenerator(IdPatriLangGenerator idGenerator, DatePatriLangGenerator dateGenerator) {
        this.idGenerator = new IdPatriLangGenerator();
        this.dateGenerator = new DatePatriLangGenerator();
    }

    @Override
    public String apply(PiecesJustificative piecesJustificative) {
        return piecesJustificativeSyntax(piecesJustificative);
    }

    private String piecesJustificativeSyntax(PiecesJustificative piecesJustificative) {
        var idFlux = idGenerator.apply(piecesJustificative.idFluxArgent());
        var dateEmission = dateGenerator.apply(piecesJustificative.dateEmission());
        var driveLink = piecesJustificative.driveLink();
        return String.format("* `%s`, %s, %s", idFlux, dateEmission, driveLink);
    }
}
