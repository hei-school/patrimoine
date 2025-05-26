package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CasContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitDevise;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsText;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;
import school.hei.patrimoine.Pair;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Possession;

@RequiredArgsConstructor
public class PatriLangCasVisitor implements Function<CasContext, Cas> {
  private final SectionVisitor sectionVisitor;

  @Override
  public Cas apply(CasContext ctx) {
    var sectionCasGeneral = ctx.sectionCasGeneral();
    var ajd =
        this.sectionVisitor
            .variableDateVisitor()
            .apply(sectionCasGeneral.ligneDateSpecification().dateValue);
    var finSimulation =
        this.sectionVisitor
            .variableDateVisitor()
            .apply(sectionCasGeneral.ligneDateFinSimulation().dateValue);
    var possesseurs = this.sectionVisitor.visitSectionPossesseurs(ctx.sectionPossesseurs());

    return new Cas(ajd, finSimulation, possesseurs) {
      @Override
      protected Devise devise() {
        return visitDevise(sectionCasGeneral.ligneDevise().devise());
      }

      @Override
      protected String nom() {
        return visitVariableAsText(sectionCasGeneral.ligneCasNom().nom);
      }

      @Override
      protected void init() {
        if (nonNull(ctx.sectionInitialisation())) {
          sectionVisitor.visitSectionInitialisation(ctx.sectionInitialisation());
        }
      }

      @Override
      protected void suivi() {
        if (nonNull(ctx.sectionSuivi())) {
          sectionVisitor.visitSectionSuivi(ctx.sectionSuivi());
        }
      }

      @Override
      public Set<Possession> possessions() {
        return collectPossessions(ctx);
      }
    };
  }

  private Set<Possession> collectPossessions(CasContext ctx) {
    Set<Possession> possessions = new HashSet<>();

    addIfPresent(ctx.sectionTresoreries(), sectionVisitor::visitSectionTresoreries, possessions);
    addIfPresent(ctx.sectionCreances(), sectionVisitor::visitSectionCreances, possessions);
    addIfPresent(ctx.sectionDettes(), sectionVisitor::visitSectionDettes, possessions);

    if (nonNull(ctx.sectionOperations())) {
      possessions.addAll(sectionVisitor.visitSectionOperations(ctx.sectionOperations()));
    }

    return possessions;
  }

  private <C extends ParserRuleContext, T extends Possession> void addIfPresent(
      C context, Function<C, Set<Pair<String, T>>> visitFunction, Set<Possession> possessions) {
    if (nonNull(context)) {
      var pairs = visitFunction.apply(context);
      possessions.addAll(mapPairs(pairs));
    }
  }

  private static <T extends Possession> Set<T> mapPairs(Set<Pair<String, T>> pairSet) {
    return pairSet.stream().map(Pair::second).collect(toSet());
  }
}
