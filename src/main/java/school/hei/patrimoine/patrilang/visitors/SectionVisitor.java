package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileCas;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import school.hei.patrimoine.Pair;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;
import school.hei.patrimoine.patrilang.visitors.possession.*;

public record SectionVisitor(
    String casSetFolderPath,
    ObjectifVisitor objectifVisitor,
    MaterielVisitor materielVisitor,
    AchatMaterielVisitor achatMaterielVisitor,
    FluxArgentVisitor fluxArgentVisitor,
    TransferArgentVisitor transferArgentVisitor,
    CorrectionVisitor correctionVisitor,
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

  public void visitSectionInitialisation(SectionInitialisationContext ctx) {
    ctx.operations().forEach(this::visitOperations);
  }

  public void visitSectionSuivi(SectionSuiviContext ctx) {
    ctx.operations().forEach(this::visitOperations);
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
    return mapCompteElements(
        ctx.compteElement(), variableCompteVisitor.getVisitor(), variableCompteVisitor);
  }

  public Set<Pair<String, Dette>> visitSectionDettes(SectionDettesContext ctx) {
    return mapCompteElements(
        ctx.compteElement(), variableDetteVisitor.getVisitor(), variableDetteVisitor);
  }

  public Set<Pair<String, Creance>> visitSectionCreances(SectionCreancesContext ctx) {
    return mapCompteElements(
        ctx.compteElement(), variableCreanceVisitor.getVisitor(), variableCreanceVisitor);
  }

  public Set<Possession> visitSectionOperations(SectionOperationsContext ctx) {
    return ctx.operations().stream().flatMap(op -> visitOperations(op).stream()).collect(toSet());
  }

  private Set<Possession> visitOperations(OperationsContext ctx) {
    var possessions =
        ctx.operation().stream()
            .map(this::visitOperation)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toSet());

    if (isNull(ctx.sousTitre())) {
      return possessions;
    }

    return Set.of(this.groupPossessionVisitor.apply(ctx.sousTitre(), possessions));
  }

  private Optional<Possession> visitOperation(OperationContext ctx) {
    if (nonNull(ctx.fluxArgentTransferer())) {
      return Optional.of(this.transferArgentVisitor.apply(ctx.fluxArgentTransferer()));
    }

    if (nonNull(ctx.fluxArgentEntrer())) {
      return Optional.of(this.fluxArgentVisitor.apply(ctx.fluxArgentEntrer()));
    }

    if (nonNull(ctx.fluxArgentSortir())) {
      return Optional.of(this.fluxArgentVisitor.apply(ctx.fluxArgentSortir()));
    }

    if (nonNull(ctx.acheterMateriel())) {
      return Optional.of(this.achatMaterielVisitor.apply(ctx.acheterMateriel()));
    }

    if (nonNull(ctx.possedeMateriel())) {
      return Optional.of(this.materielVisitor.apply(ctx.possedeMateriel()));
    }

    if (nonNull(ctx.correction())) {
      return Optional.of(this.correctionVisitor().apply(ctx.correction()));
    }

    if (nonNull(ctx.objectif())) {
      this.objectifVisitor.apply(ctx.objectif());
      return Optional.empty();
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
        new ObjectifVisitor(dateVisitor, variableCompteVisitor),
        new MaterielVisitor(dateVisitor),
        new AchatMaterielVisitor(dateVisitor, variableCompteVisitor),
        new FluxArgentVisitor(dateVisitor, variableCompteVisitor),
        new TransferArgentVisitor(dateVisitor, variableCompteVisitor),
        new CorrectionVisitor(dateVisitor, variableCompteVisitor),
        new GroupPossessionVisitor(dateVisitor),
        dateVisitor,
        personVisitor,
        variableCompteVisitor,
        variableDetteVisitor,
        variableCreanceVisitor);
  }

  private static <T extends Possession> Set<Pair<String, T>> mapCompteElements(
      List<CompteElementContext> elements,
      Function<CompteContext, T> compteVisitor,
      Function<VariableContext, T> variableVisitor) {
    return elements.stream()
        .map(
            element -> {
              if (nonNull(element.compte())) {
                return compteVisitor.apply(element.compte());
              }
              return variableVisitor.apply(element.variable());
            })
        .map(obj -> new Pair<>(obj.nom(), obj))
        .collect(toSet());
  }
}
