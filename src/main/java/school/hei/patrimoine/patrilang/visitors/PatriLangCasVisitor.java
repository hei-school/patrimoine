package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CasContext;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsDevise;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsText;

import java.util.Set;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Possession;

@RequiredArgsConstructor
public class PatriLangCasVisitor implements Function<CasContext, Cas> {
  private final SectionVisitor sectionVisitor;

  @Override
  public Cas apply(CasContext ctx) {
    var ajd =
        this.sectionVisitor
            .variableDateVisitor()
            .apply(ctx.sectionCasGeneral().ligneDateSpecification().variable());
    var finSimulation =
        this.sectionVisitor
            .variableDateVisitor()
            .apply(ctx.sectionCasGeneral().ligneDateFinSimulation().variable());
    var nom = visitVariableAsText(ctx.sectionCasGeneral().ligneCasNom().variable());
    var devise = visitVariableAsDevise(ctx.sectionCasGeneral().ligneDevise().variable());
    var possesseurs = this.sectionVisitor.visitSectionPossesseurs(ctx.sectionPossesseurs());

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
        return Set.of();
      }
    };
  }
}
