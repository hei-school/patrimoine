package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitArgent;

import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.cas.CasSet;

@RequiredArgsConstructor
public class PatriLangToutCasVisitor implements Function<ToutCasContext, CasSet> {
  private final SectionVisitor sectionVisitor;

  @Override
  public CasSet apply(ToutCasContext ctx) {
    var objectifFinal =
        visitArgent(ctx.sectionToutCasGeneral().ligneObjectifFinal().valeurComptable);

    if (nonNull(ctx.sectionDates())) {
      this.sectionVisitor.visitSectionDates(ctx.sectionDates());
    }

    if (nonNull(ctx.sectionPersonnes())) {
      this.sectionVisitor.visitSectionPersonnes(ctx.sectionPersonnes());
    }

    if (nonNull(ctx.sectionTresoreries())) {
      this.sectionVisitor.visitSectionTresoreries(ctx.sectionTresoreries());
    }

    if (nonNull(ctx.sectionCreances())) {
      this.sectionVisitor.visitSectionCreances(ctx.sectionCreances());
    }

    if (nonNull(ctx.sectionDettes())) {
      this.sectionVisitor.visitSectionDettes(ctx.sectionDettes());
    }

    var casSet = this.sectionVisitor.visitSectionCas(ctx.sectionCas());
    return new CasSet(casSet, objectifFinal);
  }
}
