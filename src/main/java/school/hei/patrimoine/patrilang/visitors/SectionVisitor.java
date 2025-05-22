package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileCas;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import school.hei.patrimoine.Pair;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.visitors.possession.*;

public record SectionVisitor(
    String casSetFolderPath,
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
    return ctx.ligneNom().stream()
        .map(ligne -> visitVariableAsText(ligne.variable()))
        .map(nom -> transpileCas(nom, this))
        .collect(toSet());
  }

  public Set<Pair<String, LocalDate>> visitSectionDates(SectionDatesContext ctx) {
    return ctx.ligneNomValeur().stream()
        .map(
            ligne ->
                new Pair<>(
                    visitVariableAsText(ligne.variable(0)),
                    this.variableDateVisitor.apply(ligne.variable(1))))
        .collect(toSet());
  }

  public Map<Personne, Double> visitSectionPossesseurs(SectionPossesseursContext ctx) {
    return ctx.lignePossesseur().stream()
        .collect(
            toMap(
                ligne -> variablePersonneVisitor.apply(ligne.variable(0)),
                ligne -> visitVariableAsNombre(ligne.variable(1))));
  }

  public Set<Pair<String, Personne>> visitSectionPersonnes(SectionPersonnesContext ctx) {
    return ctx.ligneNom().stream()
        .map(ligne -> this.variablePersonneVisitor.apply(ligne.variable()))
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

  public Set<Possession> visitSectionOperations(SectionOperationsContext ctx) {
    return ctx.operations().stream().flatMap(op -> visitOperations(op).stream()).collect(toSet());
  }

  private Set<Possession> visitOperations(OperationsContext ctx) {
    var possessions = ctx.operation().stream().map(this::visitOperation).collect(toSet());

    if (isNull(ctx.sousTitre())) {
      return possessions;
    }

    return Set.of(this.groupPossessionVisitor.apply(ctx.sousTitre(), possessions));
  }

  private Possession visitOperation(OperationContext ctx) {
    if (nonNull(ctx.fluxArgentTransferer())) {
      return this.transferArgentVisitor.apply(ctx.fluxArgentTransferer());
    }

    if (nonNull(ctx.fluxArgentEntrer())) {
      return this.fluxArgentVisitor.apply(ctx.fluxArgentEntrer());
    }

    if (nonNull(ctx.fluxArgentSortir())) {
      return this.fluxArgentVisitor.apply(ctx.fluxArgentSortir());
    }

    if (nonNull(ctx.acheterMateriel())) {
      return this.achatMaterielVisitor.apply(ctx.acheterMateriel());
    }

    if (nonNull(ctx.possedeMateriel())) {
      return this.materielVisitor.apply(ctx.possedeMateriel());
    }

    throw new IllegalArgumentException("Opération inconnue");
  }

  public static SectionVisitor create(String casSetFolderPath) {
    var dateVisitor = new VariableVisitor<>(DateContext.class, BaseVisitor::visitDate);
    var personVisitor = new VariableVisitor<>(TextContext.class, BaseVisitor::visitPersonne);

    var compteVisitor = new CompteVisitor(dateVisitor);
    var creanceVisitor = new CreanceVisitor(dateVisitor);
    var detteVisitor = new DetteVisitor(dateVisitor);

    var variableCompteVisitor = new VariableVisitor<>(CompteContext.class, compteVisitor);
    var variableCreanceVisitor = new VariableVisitor<>(CompteContext.class, creanceVisitor);
    var variableDetteVisitor = new VariableVisitor<>(CompteContext.class, detteVisitor);

    return new SectionVisitor(
        casSetFolderPath,
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
