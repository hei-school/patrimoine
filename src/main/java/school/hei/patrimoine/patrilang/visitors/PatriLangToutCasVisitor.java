package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;

import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.cas.CasSet;

@RequiredArgsConstructor
public class PatriLangToutCasVisitor implements Function<ToutCasContext, CasSet> {
  private final SectionVisitor sectionVisitor;

  @Override
  public CasSet apply(ToutCasContext ctx) {
    var objectifFinal =
        this.sectionVisitor
            .getArgentVisitor()
            .apply(ctx.sectionToutCasGeneral().ligneObjectifFinal().valeurComptable);

    if (nonNull(ctx.sectionDatesDeclarations())) {
      this.sectionVisitor.visitSectionDatesDeclarations(ctx.sectionDatesDeclarations());
    }

    if (nonNull(ctx.sectionPersonnesDeclarations())) {
      this.sectionVisitor.visitSectionPersonnesDeclarations(ctx.sectionPersonnesDeclarations());
    }

    if (nonNull(ctx.sectionTresoreries())) {
      this.sectionVisitor.visitSectionTrésoreries(ctx.sectionTresoreries());
    }

    if (nonNull(ctx.sectionCreances())) {
      this.sectionVisitor.visitSectionCréances(ctx.sectionCreances());
    }

    if (nonNull(ctx.sectionDettes())) {
      this.sectionVisitor.visitSectionDettes(ctx.sectionDettes());
    }

    var casSet = this.sectionVisitor.visitSectionCas(ctx.sectionCas());
    return new CasSet(casSet, objectifFinal);
  }
}
