package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CasContext;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsDevise;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsText;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
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
            .apply(sectionCasGeneral.ligneDateSpecification().variable());
    var finSimulation =
        this.sectionVisitor
            .variableDateVisitor()
            .apply(sectionCasGeneral.ligneDateFinSimulation().variable());
    var possesseurs = this.sectionVisitor.visitSectionPossesseurs(ctx.sectionPossesseurs());

    return new Cas(ajd, finSimulation, possesseurs) {
      @Override
      protected Devise devise() {
        return visitVariableAsDevise(sectionCasGeneral.ligneDevise().variable());
      }

      @Override
      protected String nom() {
        return visitVariableAsText(sectionCasGeneral.ligneCasNom().variable());
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

    if (nonNull(ctx.sectionTresoreries())) {
      var tresoreriesPairs = sectionVisitor.visitSectionTresoreries(ctx.sectionTresoreries());
      possessions.addAll(mapPairs(tresoreriesPairs));
    }

    if (nonNull(ctx.sectionCreances())) {
      var creancesPairs = sectionVisitor.visitSectionCreances(ctx.sectionCreances());
      possessions.addAll(mapPairs(creancesPairs));
    }

    if (nonNull(ctx.sectionDettes())) {
      var dettesPairs = sectionVisitor.visitSectionDettes(ctx.sectionDettes());
      possessions.addAll(mapPairs(dettesPairs));
    }

    if (nonNull(ctx.sectionOperations())) {
      possessions.addAll(sectionVisitor.visitSectionOperations(ctx.sectionOperations()));
    }

    return possessions;
  }

  private static <T extends Possession> Set<T> mapPairs(Set<Pair<String, T>> pairSet) {
    return pairSet.stream().map(Pair::second).collect(toSet());
  }
}
