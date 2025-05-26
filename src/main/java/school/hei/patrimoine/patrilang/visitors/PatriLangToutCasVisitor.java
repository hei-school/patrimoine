package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
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

    this.loadDatesInContainer(ctx.sectionDates());
    this.loadPersonnesInContainer(ctx.sectionPersonnes());
    this.loadTresoreriesInContainer(ctx.sectionTresoreries());
    this.loadDettesInContainer(ctx.sectionDettes());
    this.loadCreancesInContainer(ctx.sectionCreances());

    var casSet = this.sectionVisitor.visitSectionCas(ctx.sectionCas());
    return new CasSet(casSet, objectifFinal);
  }

  public void loadDatesInContainer(SectionDatesContext ctx) {
    if (isNull(ctx)) return;

    var dates = this.sectionVisitor.visitSectionDates(ctx);
    this.sectionVisitor.variableDateVisitor().storeVariables(dates);
  }

  public void loadPersonnesInContainer(SectionPersonnesContext ctx) {
    if (isNull(ctx)) return;

    var personnes = this.sectionVisitor.visitSectionPersonnes(ctx);
    this.sectionVisitor.variablePersonneVisitor().storeVariables(personnes);
  }

  public void loadTresoreriesInContainer(SectionTresoreriesContext ctx) {
    if (isNull(ctx)) return;

    var tresoreries = this.sectionVisitor.visitSectionTresoreries(ctx);
    this.sectionVisitor.variableCompteVisitor().storeVariables(tresoreries);
  }

  public void loadDettesInContainer(SectionDettesContext ctx) {
    if (isNull(ctx)) return;

    var dettes = this.sectionVisitor.visitSectionDettes(ctx);
    this.sectionVisitor.variableDetteVisitor().storeVariables(dettes);
  }

  public void loadCreancesInContainer(SectionCreancesContext ctx) {
    if (isNull(ctx)) return;

    var creances = this.sectionVisitor.visitSectionCreances(ctx);
    this.sectionVisitor.variableCreanceVisitor().storeVariables(creances);
  }
}
