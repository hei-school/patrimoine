package school.hei.patrimoine.patrilang;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DocumentContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.SectionCreancesContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.SectionDettesContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.SectionTresoreriesContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.parseNodeValue;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitDevise;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;
import school.hei.patrimoine.patrilang.modele.PossessionGetter;
import school.hei.patrimoine.patrilang.visitors.*;

public class PatriLangTranspileVisitor extends PatriLangParserBaseVisitor<Object> {
  private final CompteVisitor compteVisitor;
  private final CreanceVisitor creanceVisitor;
  private final DetteVisitor detteVisitor;
  private final MaterielVisitor materielVisitor;
  private final AchatMaterielVisitor achatMaterielVisitor;
  private final FluxArgentVisitor fluxArgentVisitor;
  private final TransferArgentVisitor transferArgentVisitor;

  private final Set<Compte> comptes;

  public PatriLangTranspileVisitor() {
    this.comptes = new HashSet<>();

    PossessionGetter<Compte> compteGetter = new PossessionGetter<>(this.comptes);

    this.detteVisitor = new DetteVisitor();
    this.compteVisitor = new CompteVisitor();
    this.creanceVisitor = new CreanceVisitor();
    this.materielVisitor = new MaterielVisitor();
    this.achatMaterielVisitor = new AchatMaterielVisitor(compteGetter);
    this.fluxArgentVisitor = new FluxArgentVisitor(compteGetter);
    this.transferArgentVisitor = new TransferArgentVisitor(compteGetter);
  }

  @Override
  public Patrimoine visitDocument(DocumentContext ctx) {
    LocalDate t = BaseVisitor.visitDate(ctx.sectionGeneral().lignePatrimoineDate().date());
    Devise devise = visitDevise(ctx.sectionGeneral().lignePatrimoineDevise().DEVISE());
    String nom = parseNodeValue(ctx.sectionGeneral().lignePatrimoineNom().TEXT());
    Personne personne = new Personne(nom);
    Set<Possession> possessions = visitPossessions(ctx);

    return Patrimoine.of(String.format("Patrimoine de %s", nom), devise, t, personne, possessions);
  }

  @Override
  public Set<Compte> visitSectionTresoreries(SectionTresoreriesContext ctx) {
    this.comptes.addAll(ctx.compte().stream().map(compteVisitor::visit).collect(toSet()));
    return this.comptes;
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
  public Set<Possession> visitSectionOperations(PatriLangParser.SectionOperationsContext ctx) {
    return ctx.operation().stream().map(this::visitOperation).collect(toSet());
  }

  @Override
  public Possession visitOperation(OperationContext ctx) {
    if (ctx.fluxArgentTransferer() != null) {
      return this.transferArgentVisitor.visit(ctx.fluxArgentTransferer());
    }

    if (ctx.fluxArgentEntrer() != null) {
      return this.fluxArgentVisitor.visit(ctx.fluxArgentEntrer());
    }

    if (ctx.fluxArgentSortir() != null) {
      return this.fluxArgentVisitor.visit(ctx.fluxArgentSortir());
    }

    if (ctx.acheterMateriel() != null) {
      return this.achatMaterielVisitor.visit(ctx.acheterMateriel());
    }

    if (ctx.possedeMateriel() != null) {
      return this.materielVisitor.visit(ctx.possedeMateriel());
    }

    throw new IllegalArgumentException("Unknown operation");
  }

  Set<Possession> visitPossessions(DocumentContext ctx) {
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
