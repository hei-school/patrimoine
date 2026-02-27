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
import school.hei.patrimoine.modele.comptable.OperationComptable;
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

    Supplier<Set<OperationComptable>> operationsSupplier =
        () -> {
          var chilSectionVisitor = sectionVisitor.createChildSectionVisitor();
          Set<OperationComptable> operations = new HashSet<>();

          if (nonNull(ctx.sectionTresoreries())) {
            chilSectionVisitor.visitSectionTrésoreries(ctx.sectionTresoreries()).stream()
                .map(OperationComptable::make)
                .forEach(operations::add);
          }

          if (nonNull(ctx.sectionCreances())) {
            chilSectionVisitor.visitSectionCréances(ctx.sectionCreances()).stream()
                .map(OperationComptable::make)
                .forEach(operations::add);
          }

          if (nonNull(ctx.sectionDettes())) {
            chilSectionVisitor.visitSectionDettes(ctx.sectionDettes()).stream()
                .map(OperationComptable::make)
                .forEach(operations::add);
          }

          if (nonNull(ctx.sectionOperations())) {
            operations.addAll(chilSectionVisitor.visitSectionOperations(ctx.sectionOperations()));
          }

          return operations;
        };

    return new PatriLangCas(
        nom, devise, ajd, finSimulation, possesseurs, init, suivi, operationsSupplier);
  }
}
