package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import java.util.*;
import school.hei.patrimoine.Pair;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.Possession;

public record SectionVisitor(
    MaterielVisitor materielVisitor,
    AchatMaterielVisitor achatMaterielVisitor,
    FluxArgentVisitor fluxArgentVisitor,
    TransferArgentVisitor transferArgentVisitor,
    GroupPossessionVisitor groupPossessionVisitor,
    VariableVisitor<DateContext, LocalDate> variableDateVisitor,
    VariableVisitor<TextContext, Personne> variablePersonneVisitor,
    VariableVisitor<CompteContext, Compte> variableCompteVisitor,
    VariableVisitor<CompteContext, Dette> variableDetteVisitor,
    VariableVisitor<CompteContext, Creance> variableCreanceVisitor) {
  public Set<Cas> visitSectionCas(SectionCasContext ctx) {
    Set<Cas> casSet = new HashSet<>();
    return casSet;
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

  public Set<Pair<String, Personne>> visitSectionPersonnes(SectionPersonnesContext ctx) {
    return ctx.ligneNom().stream()
        .map(ligneNomContext -> this.variablePersonneVisitor.apply(ligneNomContext.variable()))
        .map(personne -> new Pair<>(personne.nom(), personne))
        .collect(toSet());
  }

  public Set<Pair<String, Compte>> visitSectionTresoreries(SectionTresoreriesContext ctx) {
    return ctx.compte().stream()
        .map(this.variableCompteVisitor.getVisitor())
        .map(compte -> new Pair<>(compte.nom(), compte))
        .collect(toSet());
  }

  public Set<Pair<String, Dette>> visitSectionDettes(SectionDettesContext ctx) {
    return ctx.compte().stream()
        .map(this.variableDetteVisitor.getVisitor())
        .map(dette -> new Pair<>(dette.nom(), dette))
        .collect(toSet());
  }

  public Set<Pair<String, Creance>> visitSectionCreances(SectionCreancesContext ctx) {
    return ctx.compte().stream()
        .map(this.variableCreanceVisitor.getVisitor())
        .map(creance -> new Pair<>(creance.nom(), creance))
        .collect(toSet());
  }

  private Set<Possession> visitSectionOperations(SectionOperationsContext ctx) {
    Set<Possession> possessions = new HashSet<>();

    for (OperationsContext operationsContext : ctx.operations()) {
      possessions.addAll(visitOperations(operationsContext));
    }
    return possessions;
  }

  private Set<Possession> visitOperations(OperationsContext ctx) {
    Set<Possession> possessions =
        ctx.operation().stream().map(this::visitOperation).collect(toSet());

    if (isNull(ctx.sousTitre())) {
      return possessions;
    }

    return Set.of(this.groupPossessionVisitor.apply(ctx.sousTitre(), possessions));
  }

  private Possession visitOperation(OperationContext ctx) {
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

  public static SectionVisitor create() {
    var dateVisitor = new VariableVisitor<>(DateContext.class, BaseVisitor::visitDate);
    var personVisitor = new VariableVisitor<>(TextContext.class, BaseVisitor::visitPersonne);

    var compteVisitor = new CompteVisitor(dateVisitor);
    var creanceVisitor = new CreanceVisitor(dateVisitor);
    var detteVisitor = new DetteVisitor(dateVisitor);

    var variableCompteVisitor = new VariableVisitor<>(CompteContext.class, compteVisitor);
    var variableCreanceVisitor = new VariableVisitor<>(CompteContext.class, creanceVisitor);
    var variableDetteVisitor = new VariableVisitor<>(CompteContext.class, detteVisitor);

    return new SectionVisitor(
        new MaterielVisitor(dateVisitor),
        new AchatMaterielVisitor(dateVisitor, variableCompteVisitor),
        new FluxArgentVisitor(dateVisitor, variableCompteVisitor),
        new TransferArgentVisitor(dateVisitor, variableCompteVisitor),
        new GroupPossessionVisitor(dateVisitor),
        dateVisitor,
        personVisitor,
        variableCompteVisitor,
        variableDetteVisitor,
        variableCreanceVisitor);
  }
}
