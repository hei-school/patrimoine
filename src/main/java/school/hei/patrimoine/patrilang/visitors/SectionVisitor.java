package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import java.util.*;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.Pair;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;

@RequiredArgsConstructor
public class SectionVisitor {
  private final MaterielVisitor materielVisitor;
  private final AchatMaterielVisitor achatMaterielVisitor;
  private final FluxArgentVisitor fluxArgentVisitor;
  private final TransferArgentVisitor transferArgentVisitor;
  private final GroupPossessionVisitor groupPossessionVisitor;
  private final VariableVisitor<DateContext, LocalDate> variableDateVisitor;
  private final VariableVisitor<TextContext, Personne> variablePersonneVisitor;
  private final VariableVisitor<CompteContext, Compte> variableCompteVisitor;
  private final VariableVisitor<CompteContext, Dette> variableDetteVisitor;
  private final VariableVisitor<CompteContext, Creance> variableCreanceVisitor;

  public Set<String> visitSectionCas(SectionCasContext ctx) {
    return ctx.ligneNom().stream()
        .map(ligneNomContext -> visitVariableAsText(ligneNomContext.variable()))
        .collect(toSet());
  }

  public Set<Pair<String, LocalDate>> visitSectionDates(SectionDatesContext ctx) {
    return ctx.ligneNomValeur().stream()
        .map(
            ligneNomValeurContext ->
                new Pair<>(
                    visitVariableAsText(ligneNomValeurContext.variable(0)),
                    this.variableDateVisitor.apply(ligneNomValeurContext.variable(1))))
        .collect(toSet());
  }

  public Map<Personne, Double> visitSectionPossesseurs(SectionPossesseursContext ctx) {
    Map<Personne, Double> dates = new HashMap<>();
    for (var ligneNomValeurContext : ctx.lignePossesseur()) {
      dates.put(
          this.variablePersonneVisitor.apply(ligneNomValeurContext.variable(0)),
          visitVariableAsNombre(ligneNomValeurContext.variable(1)));
    }
    return dates;
  }

  public Set<Personne> visitSectionPersonnes(SectionPersonnesContext ctx) {
    return ctx.ligneNom().stream()
        .map(ligneNomContext -> this.variablePersonneVisitor.apply(ligneNomContext.variable()))
        .collect(toSet());
  }

  public Set<Compte> visitSectionTresoreries(SectionTresoreriesContext ctx) {
    return ctx.compte().stream().map(this.variableCompteVisitor.getVisitor()).collect(toSet());
  }

  public Set<Dette> visitSectionDettes(SectionDettesContext ctx) {
    return ctx.compte().stream().map(this.variableDetteVisitor.getVisitor()).collect(toSet());
  }

  public Set<Creance> visitSectionCreances(SectionDettesContext ctx) {
    return ctx.compte().stream().map(this.variableCreanceVisitor.getVisitor()).collect(toSet());
  }

  private Set<Possession> visitSectionOperations(SectionOperationsContext ctx) {
    Set<Possession> possessions = new HashSet<>();

    for (PatriLangParser.OperationsContext operationsContext : ctx.operations()) {
      possessions.addAll(visitOperations(operationsContext));
    }
    return possessions;
  }

  private Set<Possession> visitOperations(PatriLangParser.OperationsContext ctx) {
    Set<Possession> possessions =
        ctx.operation().stream().map(this::visitOperation).collect(toSet());

    if (isNull(ctx.sousTitre())) {
      return possessions;
    }

    return Set.of(this.groupPossessionVisitor.apply(ctx.sousTitre(), possessions));
  }

  private Possession visitOperation(PatriLangParser.OperationContext ctx) {
    if (!isNull(ctx.fluxArgentTransferer())) {
      return this.transferArgentVisitor.apply(ctx.fluxArgentTransferer());
    }

    if (!isNull(ctx.fluxArgentEntrer())) {
      return this.fluxArgentVisitor.apply(ctx.fluxArgentEntrer());
    }

    if (!isNull(ctx.fluxArgentSortir())) {
      return this.fluxArgentVisitor.apply(ctx.fluxArgentSortir());
    }

    if (!isNull(ctx.acheterMateriel())) {
      return this.achatMaterielVisitor.apply(ctx.acheterMateriel());
    }

    if (!isNull(ctx.possedeMateriel())) {
      return this.materielVisitor.apply(ctx.possedeMateriel());
    }

    throw new IllegalArgumentException("Unknown operation");
  }
}
