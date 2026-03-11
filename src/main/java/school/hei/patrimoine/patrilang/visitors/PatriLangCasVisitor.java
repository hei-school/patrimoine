package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CasContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitDevise;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitText;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.modele.PatriLangCas;

@RequiredArgsConstructor
public class PatriLangCasVisitor implements Function<CasContext, PatriLangCas> {
  private final SectionVisitor sectionVisitor;

  @Override
  public PatriLangCas apply(CasContext ctx) {
    var sectionCasGeneral = ctx.sectionCasGeneral();
    var nom = visitText(sectionCasGeneral.ligneCasNom().nom);
    var devise = visitDevise(sectionCasGeneral.ligneDevise().devise());
    var ajd =
        this.sectionVisitor
            .getVariableVisitor()
            .asDate(sectionCasGeneral.ligneDateSpecification().dateValue);
    var finSimulation =
        this.sectionVisitor
            .getVariableVisitor()
            .asDate(sectionCasGeneral.ligneDateFinSimulation().dateValue);

    var possesseurs = this.sectionVisitor.visitSectionPossesseurs(ctx.sectionPossesseurs());

    if (nonNull(ctx.sectionNombresDeclarations())) {
      this.sectionVisitor.visitSectionNombresDeclarations(ctx.sectionNombresDeclarations());
    }

    if (nonNull(ctx.sectionDatesDeclarations())) {
      this.sectionVisitor.visitSectionDatesDeclarations(ctx.sectionDatesDeclarations());
    }

    if (nonNull(ctx.sectionOperationTemplateDeclaration())) {
      this.sectionVisitor.visitOperationTemplateDeclarations(
          ctx.sectionOperationTemplateDeclaration());
    }

    Runnable init =
        () -> {
          var chilSectionVisitor = sectionVisitor.createChildSectionVisitor();
          if (nonNull(ctx.sectionInitialisation())) {
            chilSectionVisitor.visitSectionInitialisation(ctx.sectionInitialisation());
          }
        };

    Runnable suivi =
        () -> {
          var chilSectionVisitor = sectionVisitor.createChildSectionVisitor();
          if (nonNull(ctx.sectionSuivi())) {
            chilSectionVisitor.visitSectionSuivi(ctx.sectionSuivi());
          }
        };

    Supplier<Set<Possession>> possessionsSupplier =
        () -> {
          var chilSectionVisitor = sectionVisitor.createChildSectionVisitor();
          Set<Possession> possessions = new HashSet<>();

          if (nonNull(ctx.sectionTresoreries())) {
            possessions.addAll(
                chilSectionVisitor.visitSectionTrésoreries(ctx.sectionTresoreries()));
          }

          if (nonNull(ctx.sectionCreances())) {
            possessions.addAll(chilSectionVisitor.visitSectionCréances(ctx.sectionCreances()));
          }

          if (nonNull(ctx.sectionDettes())) {
            possessions.addAll(chilSectionVisitor.visitSectionDettes(ctx.sectionDettes()));
          }

          if (nonNull(ctx.sectionOperations())) {
            possessions.addAll(chilSectionVisitor.visitSectionOperations(ctx.sectionOperations()));
          }

          return possessions;
        };

    return new PatriLangCas(
        nom, devise, ajd, finSimulation, possesseurs, init, suivi, possessionsSupplier);
  }
}
