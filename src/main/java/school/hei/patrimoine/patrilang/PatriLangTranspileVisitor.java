package school.hei.patrimoine.patrilang;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;
import school.hei.patrimoine.patrilang.modele.PossessionAccumulator;
import school.hei.patrimoine.patrilang.visitors.*;

@RequiredArgsConstructor
public class PatriLangTranspileVisitor extends PatriLangParserBaseVisitor<Object> {
  private final PossessionAccumulator<Compte> compteAccumulator = new PossessionAccumulator<>();
  private final CompteVisitor compteVisitor;
  private final CreanceVisitor creanceVisitor;
  private final DetteVisitor detteVisitor;
  private final MaterielVisitor materielVisitor;
  private final AchatMaterielVisitor achatMaterielVisitor;
  private final FluxArgentVisitor fluxArgentVisitor;
  private final TransferArgentVisitor transferArgentVisitor;
  private final GroupPossessionVisitor groupPossessionVisitor;

  @Override
  public Object visitDocument(DocumentContext ctx) {
    if (!isNull(ctx.patrimoine())) {
      return this.visitPatrimoine(ctx.patrimoine());
    }

    return this.visitCas(ctx.cas());
  }

  @Override
  public Patrimoine visitPatrimoine(PatrimoineContext ctx) {
    LocalDate t =
        VariableVisitor.visitVariable(
            ctx.sectionPatrimoineGeneral().lignePatrimoineDate().variable(),
            DateContext.class,
            BaseVisitor::visitDate);
    Devise devise =
        VariableVisitor.visitVariable(
            ctx.sectionPatrimoineGeneral().ligneDevise().variable(),
            DeviseContext.class,
            BaseVisitor::visitDevise);
    String nom =
        VariableVisitor.visitVariable(
            ctx.sectionPatrimoineGeneral().lignePatrimoineNom().variable(),
            TextContext.class,
            BaseVisitor::visitText);
    Personne personne = new Personne(nom);
    Set<Possession> possessions = visitPossessions(ctx);

    return Patrimoine.of(String.format("Patrimoine de %s", nom), devise, t, personne, possessions);
  }

  @Override
  public Set<Compte> visitSectionTresoreries(SectionTresoreriesContext ctx) {
    return this.compteAccumulator.add(
        ctx.compte().stream().map(compteVisitor::visit).collect(toSet()));
  }

  @Override
  public Set<Creance> visitSectionCreances(SectionCreancesContext ctx) {
    return ctx.compte().stream().map(creanceVisitor::visit).collect(toSet());
  }

  @Override
  public Set<Dette> visitSectionDettes(SectionDettesContext ctx) {
    return ctx.compte().stream().map(detteVisitor::visit).collect(toSet());
  }

  @Override
  public Set<Possession> visitSectionOperations(SectionOperationsContext ctx) {
    Set<Possession> possessions = new HashSet<>();

    for (OperationsContext operationsContext : ctx.operations()) {
      possessions.addAll(visitOperations(operationsContext));
    }
    return possessions;
  }

  @Override
  public Set<Possession> visitOperations(OperationsContext ctx) {
    Set<Possession> possessions =
        ctx.operation().stream().map(this::visitOperation).collect(toSet());

    if (ctx.sousTitre() == null) {
      return possessions;
    }

    return Set.of(this.groupPossessionVisitor.visit(ctx.sousTitre(), possessions));
  }

  @Override
  public Possession visitOperation(OperationContext ctx) {
    if (ctx.fluxArgentTransferer() != null) {
      return this.transferArgentVisitor.visit(
          ctx.fluxArgentTransferer(), this.compteAccumulator.getPossessionGetter());
    }

    if (ctx.fluxArgentEntrer() != null) {
      return this.fluxArgentVisitor.visit(
          ctx.fluxArgentEntrer(), this.compteAccumulator.getPossessionGetter());
    }

    if (ctx.fluxArgentSortir() != null) {
      return this.fluxArgentVisitor.visit(
          ctx.fluxArgentSortir(), this.compteAccumulator.getPossessionGetter());
    }

    if (ctx.acheterMateriel() != null) {
      return this.achatMaterielVisitor.visit(
          ctx.acheterMateriel(), this.compteAccumulator.getPossessionGetter());
    }

    if (ctx.possedeMateriel() != null) {
      return this.materielVisitor.visit(ctx.possedeMateriel());
    }

    throw new IllegalArgumentException("Unknown operation");
  }

  Set<Possession> visitPossessions(PatrimoineContext ctx) {
    Set<Possession> possessions = new HashSet<>();

    if (ctx.sectionTresoreries() != null) {
      possessions.addAll(visitSectionTresoreries(ctx.sectionTresoreries()));
    }

    if (ctx.sectionCreances() != null) {
      possessions.addAll(visitSectionCreances(ctx.sectionCreances()));
    }

    if (ctx.sectionDettes() != null) {
      possessions.addAll(visitSectionDettes(ctx.sectionDettes()));
    }

    if (ctx.sectionOperations() != null) {
      possessions.addAll(visitSectionOperations(ctx.sectionOperations()));
    }

    return possessions;
  }
}
