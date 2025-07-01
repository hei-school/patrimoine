package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileCas;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;
import school.hei.patrimoine.patrilang.factory.SectionVisitorFactory;
import school.hei.patrimoine.patrilang.modele.variable.VariableType;
import school.hei.patrimoine.patrilang.visitors.possession.*;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@Builder
@Getter
public class SectionVisitor {
  private final String casSetFolderPath;
  private final VariableVisitor variableVisitor;
  private final CompteVisitor compteVisitor;
  private final CreanceVisitor creanceVisitor;
  private final DetteVisitor detteVisitor;
  private final OperationVisitor operationVisitor;
  private final OperationTemplateVisitor operationTemplateVisitor;

  public SectionVisitor createChildSectionVisitor() {
    return SectionVisitorFactory.make(
        this.casSetFolderPath, Optional.of(this.variableVisitor.getVariableScope()));
  }

  public Set<Cas> visitSectionCas(SectionCasContext ctx) {
    return ctx.ligneNom().stream()
        .map(ligne -> visitText(ligne.nom))
        .map(
            nom -> {
              var cas = transpileCas(nom, this.createChildSectionVisitor());
              this.variableVisitor.addToScope(nom, CAS, cas);
              return cas;
            })
        .collect(toSet());
  }

  public void visitSectionDatesDeclarations(SectionDatesDeclarationsContext ctx) {
    this.operationVisitor.apply(ctx.operations(), variableVisitor);
  }

  public void visitSectionNombresDeclarations(SectionNombresDeclarationsContext ctx) {
    this.operationVisitor.apply(ctx.operations(), variableVisitor);
  }

  public void visitPersonnesMoralesDeclarations(SectionPersonnesMoralesDeclarationsContext ctx) {
    ctx.ligneNom()
        .forEach(
            ligne -> {
              var personneMorale = visitPersonneMorale(ligne.nom);
              this.variableVisitor.addToScope(
                  personneMorale.nom(), PERSONNE_MORALE, personneMorale);
            });
  }

  public void visitSectionPersonnesDeclarations(SectionPersonnesDeclarationsContext ctx) {
    ctx.ligneNom()
        .forEach(
            ligne -> {
              var personne = visitPersonne(ligne.nom);
              this.variableVisitor.addToScope(personne.nom(), PERSONNE, personne);
            });
  }

  public void visitOperationTemplateDeclarations(SectionOperationTemplateDeclarationContext ctx) {
    ctx.operationTemplate()
        .forEach(
            operation -> {
              var operationTemplate = this.operationTemplateVisitor.apply(operation);
              this.variableVisitor.addToScope(
                  operationTemplate.name(), OPERATION_TEMPLATE, operationTemplate);
            });
  }

  public void visitSectionInitialisation(SectionInitialisationContext ctx) {
    this.operationVisitor.apply(ctx.operations(), variableVisitor);
  }

  public void visitSectionSuivi(SectionSuiviContext ctx) {
    this.operationVisitor.apply(ctx.operations(), variableVisitor);
  }

  public Map<Personne, Double> visitSectionPossesseurs(SectionPossesseursContext ctx) {
    return ctx.lignePossesseur().stream()
        .collect(
            toMap(
                ligne -> variableVisitor.asPersonne(ligne.nom),
                ligne -> variableVisitor.asNombre(ligne.pourcentage) / 100));
  }

  public Set<Compte> visitSectionTrésoreries(SectionTresoreriesContext ctx) {
    return visitCompteElements(
        TRESORERIES, ctx.compteElement(), this.compteVisitor, this.variableVisitor::asCompte);
  }

  public Set<Creance> visitSectionCréances(SectionCreancesContext ctx) {
    return visitCompteElements(
        CREANCE, ctx.compteElement(), this.creanceVisitor, this.variableVisitor::asCreance);
  }

  public Set<Dette> visitSectionDettes(SectionDettesContext ctx) {
    return visitCompteElements(
        DETTE, ctx.compteElement(), this.detteVisitor, this.variableVisitor::asDette);
  }

  public Set<Possession> visitSectionOperations(SectionOperationsContext ctx) {
    return this.operationVisitor.apply(ctx.operations(), variableVisitor);
  }

  private <T extends Possession> Set<T> visitCompteElements(
      VariableType type,
      List<CompteElementContext> elements,
      SimpleVisitor<CompteContext, T> visitor,
      SimpleVisitor<VariableContext, T> variableGetter) {
    return elements.stream()
        .map(
            element -> {
              if (isNull(element.compte())) {
                return variableGetter.apply(element.variable());
              }

              var value = visitor.apply(element.compte());
              this.variableVisitor.addToScope(value.nom(), type, value);
              return value;
            })
        .collect(toSet());
  }
}
