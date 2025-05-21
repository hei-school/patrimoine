package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsArgent;

import java.util.function.Function;
import lombok.SneakyThrows;
import school.hei.patrimoine.cas.CasSet;

public class PatriLangToutCasVisitor implements Function<ToutCasContext, CasSet> {
  private final SectionVisitor sectionVisitor;

  public PatriLangToutCasVisitor(SectionVisitor sectionVisitor) {
    this.sectionVisitor = sectionVisitor;
  }

  @Override
  @SneakyThrows
  public CasSet apply(ToutCasContext ctx) {
    var objectifFinal =
        visitVariableAsArgent(ctx.sectionToutCasGeneral().ligneObjectifFinal().variable());

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
    this.sectionVisitor.variableDateVisitor().addAll(dates);
  }

  public void loadPersonnesInContainer(SectionPersonnesContext ctx) {
    if (isNull(ctx)) return;

    var personnes = this.sectionVisitor.visitSectionPersonnes(ctx);
    this.sectionVisitor.variablePersonneVisitor().addAll(personnes);
  }

  public void loadTresoreriesInContainer(SectionTresoreriesContext ctx) {
    if (isNull(ctx)) return;

    var tresoreries = this.sectionVisitor.visitSectionTresoreries(ctx);
    this.sectionVisitor.variableCompteVisitor().addAll(tresoreries);
  }

  public void loadDettesInContainer(SectionDettesContext ctx) {
    if (isNull(ctx)) return;

    var dettes = this.sectionVisitor.visitSectionDettes(ctx);
    this.sectionVisitor.variableDetteVisitor().addAll(dettes);
  }

  public void loadCreancesInContainer(SectionCreancesContext ctx) {
    if (isNull(ctx)) return;

    var creances = this.sectionVisitor.visitSectionCreances(ctx);
    this.sectionVisitor.variableCreanceVisitor().addAll(creances);
  }
}
