package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationsContext;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.extractVariableName;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.extractVariableType;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import lombok.Builder;
import lombok.Getter;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.visitors.possession.*;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@Builder
@Getter
public class OperationVisitor
    implements BiFunction<List<OperationsContext>, VariableVisitor, Set<Possession>> {
  private final VariableVisitor variableVisitor;
  private final MaterielVisitor materielVisitor;
  private final ObjectifVisitor objectifVisitor;
  private final CorrectionVisitor correctionVisitor;
  private final FluxArgentVisitor fluxArgentVisitor;
  private final AchatMaterielVisitor achatMaterielVisitor;
  private final TransferArgentVisitor transferArgentVisitor;
  private final RemboursementDetteVisitor remboursementDetteVisitor;
  private final GroupPossessionVisitor groupPossessionVisitor;
  private final OperationTemplateCallVisitor operationTemplateCallVisitor;
  private final ValeurMarcheVisitor valeurMarcheVisitor;
  private final VenteVisitor venteVisitor;

  @Override
  public Set<Possession> apply(List<OperationsContext> contexts, VariableVisitor variableVisitor) {
    return contexts.stream()
        .flatMap(op -> visitOperations(op, variableVisitor).stream())
        .collect(toSet());
  }

  private Set<Possession> visitOperations(OperationsContext ctx, VariableVisitor variableVisitor) {
    var possessions =
        ctx.operation().stream()
            .map(operationContext -> visitOperation(operationContext, variableVisitor))
            .flatMap(Set::stream)
            .collect(toSet());

    if (isNull(ctx.sousTitre())) {
      return possessions;
    }

    return Set.of(this.groupPossessionVisitor.apply(ctx.sousTitre(), possessions));
  }

  private Set<Possession> visitOperation(OperationContext ctx, VariableVisitor variableVisitor) {
    if (nonNull(ctx.fluxArgentTransferer())) {
      return Set.of(this.transferArgentVisitor.apply(ctx.fluxArgentTransferer()));
    }

    if (nonNull(ctx.fluxArgentEntrer())) {
      return Set.of(this.fluxArgentVisitor.apply(ctx.fluxArgentEntrer()));
    }

    if (nonNull(ctx.fluxArgentSortir())) {
      return Set.of(this.fluxArgentVisitor.apply(ctx.fluxArgentSortir()));
    }

    if (nonNull(ctx.acheterMateriel())) {
      return Set.of(this.achatMaterielVisitor.apply(ctx.acheterMateriel()));
    }

    if (nonNull(ctx.possedeMateriel())) {
      return Set.of(this.materielVisitor.apply(ctx.possedeMateriel()));
    }

    if (nonNull(ctx.correction())) {
      return Set.of(this.correctionVisitor.apply(ctx.correction()));
    }

    if (nonNull(ctx.operationTemplateCall())) {
      return this.operationTemplateCallVisitor.apply(ctx.operationTemplateCall());
    }

    if (nonNull(ctx.rembourserDette())) {
      return Set.of(this.remboursementDetteVisitor.apply(ctx.rembourserDette()));
    }

    if (nonNull(ctx.objectif())) {
      this.objectifVisitor.apply(ctx.objectif());
      return Set.of();
    }

    if (nonNull(ctx.ligneCasOperations())) {
      var cas = this.variableVisitor.asCas(ctx.ligneCasOperations().variable());
      return cas.possessions();
    }

    if (nonNull(ctx.ligneVariableDeclaration())) {
      var nom = extractVariableName(ctx.ligneVariableDeclaration().nomEtType.getText());
      var type = extractVariableType(ctx.ligneVariableDeclaration().nomEtType.getText());

      variableVisitor.addToScope(
          nom, type, variableVisitor.apply(ctx.ligneVariableDeclaration().valeur).value());
      return Set.of();
    }

    if (nonNull(ctx.valeurMarche())) {
      this.valeurMarcheVisitor.apply(ctx.valeurMarche());
      return Set.of();
    }

    if (nonNull(ctx.vente())) {
      this.venteVisitor.apply(ctx.vente());
      return Set.of();
    }

    throw new IllegalArgumentException("Opération inconnue");
  }
}
