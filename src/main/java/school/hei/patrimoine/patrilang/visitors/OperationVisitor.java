package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationsContext;

import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.visitors.possession.*;

@Builder
@Getter
public class OperationVisitor implements SimpleVisitor<List<OperationsContext>, Set<Possession>> {
  private final MaterielVisitor materielVisitor;
  private final ObjectifVisitor objectifVisitor;
  private final CorrectionVisitor correctionVisitor;
  private final FluxArgentVisitor fluxArgentVisitor;
  private final AchatMaterielVisitor achatMaterielVisitor;
  private final TransferArgentVisitor transferArgentVisitor;
  private final GroupPossessionVisitor groupPossessionVisitor;
  private final OperationTemplateCallVisitor operationTemplateCallVisitor;

  @Override
  public Set<Possession> apply(List<OperationsContext> contexts) {
    return contexts.stream().flatMap(op -> visitOperations(op).stream()).collect(toSet());
  }

  private Set<Possession> visitOperations(OperationsContext ctx) {
    var possessions =
        ctx.operation().stream().map(this::visitOperation).flatMap(Set::stream).collect(toSet());

    if (isNull(ctx.sousTitre())) {
      return possessions;
    }

    return Set.of(this.groupPossessionVisitor.apply(ctx.sousTitre(), possessions));
  }

  private Set<Possession> visitOperation(PatriLangParser.OperationContext ctx) {
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

    if (nonNull(ctx.objectif())) {
      this.objectifVisitor.apply(ctx.objectif());
      return Set.of();
    }

    throw new IllegalArgumentException("Opération inconnue");
  }
}
