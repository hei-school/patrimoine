package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CasContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitDevise;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitText;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Possession;

@RequiredArgsConstructor
public class PatriLangCasVisitor implements Function<CasContext, Cas> {
  private final VariableVisitor variableVisitor;
  private final SectionVisitor sectionVisitor;

  @Override
  public Cas apply(CasContext ctx) {
    var sectionCasGeneral = ctx.sectionCasGeneral();

    var nom = visitText(sectionCasGeneral.ligneCasNom().nom);
    var devise = visitDevise(sectionCasGeneral.ligneDevise().devise());
    var ajd = this.variableVisitor.asDate(sectionCasGeneral.ligneDateSpecification().dateValue);
    var finSimulation =
        this.variableVisitor.asDate(sectionCasGeneral.ligneDateFinSimulation().dateValue);
    var possesseurs = this.sectionVisitor.visitSectionPossesseurs(ctx.sectionPossesseurs());
    var possessions = collectPossessions(ctx);

    if (nonNull(ctx.sectionInitialisation())) {
      sectionVisitor.visitSectionInitialisation(ctx.sectionInitialisation());
    }

    if (nonNull(ctx.sectionSuivi())) {
      sectionVisitor.visitSectionSuivi(ctx.sectionSuivi());
    }

    return new Cas(ajd, finSimulation, possesseurs) {
      @Override
      protected Devise devise() {
        return devise;
      }

      @Override
      protected String nom() {
        return nom;
      }

      @Override
      protected void init() {}

      @Override
      protected void suivi() {}

      @Override
      public Set<Possession> possessions() {
        return possessions;
      }
    };
  }

  private Set<Possession> collectPossessions(CasContext ctx) {
    Set<Possession> possessions = new HashSet<>();

    if (nonNull(ctx.sectionTresoreries())) {
      possessions.addAll(sectionVisitor.visitSectionTrésoreries(ctx.sectionTresoreries()));
    }

    if (nonNull(ctx.sectionCreances())) {
      possessions.addAll(sectionVisitor.visitSectionCréances(ctx.sectionCreances()));
    }

    if (nonNull(ctx.sectionDettes())) {
      possessions.addAll(sectionVisitor.visitSectionDettes(ctx.sectionDettes()));
    }

    if (nonNull(ctx.sectionOperations())) {
      sectionVisitor.visitSectionOperations(ctx.sectionOperations());
    }

    return possessions;
  }
}
