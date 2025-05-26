package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileCas;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitNombre;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
        .map(ligne -> visitVariableAsText(ligne.nom))
        .map(nom -> transpileCas(nom, this))
        .collect(toSet());
  }

  public Set<Pair<String, LocalDate>> visitSectionDates(SectionDatesContext ctx) {
    return ctx.ligneDate().stream()
        .map(
            ligne ->
                new Pair<>(
                    visitVariableAsText(ligne.nom),
                    this.variableDateVisitor.apply(ligne.dateValue)))
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
                ligne -> variablePersonneVisitor.apply(ligne.nom),
                ligne -> visitNombre(ligne.pourcentage)));
  }

  public Set<Pair<String, Personne>> visitSectionPersonnes(SectionPersonnesContext ctx) {
    return ctx.ligneNom().stream()
        .map(ligne -> this.variablePersonneVisitor.apply(ligne.nom))
        .map(personne -> new Pair<>(personne.nom(), personne))
        .collect(toSet());
  }

  public Set<Pair<String, Compte>> visitSectionTresoreries(SectionTresoreriesContext ctx) {
    return visitCompteElements(ctx.compteElement(), variableCompteVisitor);
  }

  public Set<Pair<String, Dette>> visitSectionDettes(SectionDettesContext ctx) {
    return visitCompteElements(ctx.compteElement(), variableDetteVisitor);
  }

  public Set<Pair<String, Creance>> visitSectionCreances(SectionCreancesContext ctx) {
    return visitCompteElements(ctx.compteElement(), variableCreanceVisitor);
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

  private <T extends Compte> Set<Pair<String, T>> visitCompteElements(
      List<CompteElementContext> elements, VariableVisitor<CompteContext, T> visitor) {
    return elements.stream()
        .map(
            element -> {
              if (nonNull(element.compte())) {
                return visitor.apply(element.compte());
              }
              return visitor.apply(element.variable());
            })
        .map(obj -> new Pair<>(obj.nom(), obj))
        .collect(toSet());
  }
}
