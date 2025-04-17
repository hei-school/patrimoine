package school.hei.patrimoine.patrilang;

import static java.util.stream.Collectors.toSet;
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
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;
import school.hei.patrimoine.patrilang.visitors.BaseVisitor;
import school.hei.patrimoine.patrilang.visitors.CompteVisitor;
import school.hei.patrimoine.patrilang.visitors.CreanceVisitor;

public class PatriLangTranspileVisitor extends PatriLangParserBaseVisitor<Object> {
  private Set<Compte> comptes = new HashSet<>();

  @Override
  public Patrimoine visitDocument(PatriLangParser.DocumentContext ctx) {
    LocalDate t = BaseVisitor.visitDate(ctx.sectionGeneral().lignePatrimoineDate().date());
    String nom = parseNodeValue(ctx.sectionGeneral().lignePatrimoineNom().TEXT());
    Devise devise = visitDevise(ctx.sectionGeneral().lignePatrimoineDevise().DEVISE());
    Personne personne = new Personne(nom);
    Set<Possession> possessions = visitPossessions(ctx);

    return Patrimoine.of(String.format("Patrimoine De %s", nom), devise, t, personne, possessions);
  }

  @Override
  public Set<Compte> visitSectionTresorerie(PatriLangParser.SectionTresorerieContext ctx) {
    this.comptes = ctx.compte().stream().map(CompteVisitor::visitCompte).collect(toSet());
    return this.comptes;
  }

  @Override
  public Set<Creance> visitSectionCreance(PatriLangParser.SectionCreanceContext ctx) {
    return ctx.creance().stream().map(CreanceVisitor::visitCreance).collect(toSet());
  }

  Set<Possession> visitPossessions(PatriLangParser.DocumentContext ctx) {
    Set<Possession> possessions = new HashSet<>();

    possessions.addAll(visitSectionTresorerie(ctx.sectionTresorerie()));
    possessions.addAll(visitSectionCreance(ctx.sectionCreance()));
    return possessions;
  }

  public Compte findCompteByNom(String nom) {
    return this.comptes.stream()
        .filter(compte -> compte.nom().equals(nom))
        .findFirst()
        .orElseThrow();
  }
}
