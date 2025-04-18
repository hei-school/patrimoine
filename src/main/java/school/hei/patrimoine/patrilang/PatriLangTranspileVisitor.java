package school.hei.patrimoine.patrilang;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DocumentContext;
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
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;
import school.hei.patrimoine.patrilang.visitors.BaseVisitor;
import school.hei.patrimoine.patrilang.visitors.CompteVisitor;
import school.hei.patrimoine.patrilang.visitors.CreanceVisitor;
import school.hei.patrimoine.patrilang.visitors.DetteVisitor;

public class PatriLangTranspileVisitor extends PatriLangParserBaseVisitor<Object> {
  private final CompteVisitor compteVisitor;
  private final CreanceVisitor creanceVisitor;
  private final DetteVisitor detteVisitor;
  private final Set<Compte> comptes;

  public PatriLangTranspileVisitor() {
    this.comptes = new HashSet<>();
    this.compteVisitor = new CompteVisitor();
    this.creanceVisitor = new CreanceVisitor();
    this.detteVisitor = new DetteVisitor();
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

    return possessions;
  }

  public Compte findCompteByNom(String nom) {
    return this.comptes.stream()
        .filter(compte -> compte.nom().equals(nom))
        .findFirst()
        .orElseThrow();
  }
}
