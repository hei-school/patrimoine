package school.hei.patrimoine.patrilang;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.Devise.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.antlr.v4.runtime.tree.TerminalNode;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.antlr.PatriLangBaseVisitor;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.model.MaterielAppreciationType;

public class PatriLangTranspileVisitor extends PatriLangBaseVisitor<Object> {
  private Set<Compte> comptes = new HashSet<>();

    @Override
  public Patrimoine visitPatrimoine(PatriLangParser.PatrimoineContext ctx) {
    Personne personne = new Personne(visitNom(ctx.nom()));
    return Patrimoine.of(
        "Patrimoine de " + visitNom(ctx.nom()),
        visitDevise(ctx.mainDevise().DEVISE()),
        visitDate(ctx.date()),
            personne,
        collectPossessions(ctx));
  }

  @Override
  public LocalDate visitDate(PatriLangParser.DateContext ctx) {
    int jour = visitInt(ctx.NUMBER(0));
    int mois = visitInt(ctx.NUMBER(1)) - 1;
    int annee = visitInt(ctx.NUMBER(2));

    return LocalDate.of(annee, mois, jour);
  }

  @Override
  public String visitNom(PatriLangParser.NomContext ctx) {
    return ctx.MOT().getText();
  }

  @Override
  public Argent visitArgent(PatriLangParser.ArgentContext ctx) {
    return new Argent(visitDouble(ctx.NUMBER()), visitDevise(ctx.DEVISE()));
  }

  @Override
  public Set<Compte> visitComptes(PatriLangParser.ComptesContext ctx) {
    return ctx.compte().stream().map(this::visitCompte).collect(toSet());
  }

  @Override
  public Compte visitCompte(PatriLangParser.CompteContext ctx) {
    Argent valeurComptable = visitArgent(ctx.argent());
    String nomCompte = ctx.MOT().getText();
    LocalDate date = visitDate(ctx.date());

    return new Compte(nomCompte, date, date, valeurComptable);
  }

  @Override
  public FluxArgent visitFluxArgent(PatriLangParser.FluxArgentContext ctx) {
    if (ctx.getText().contains("vers")) {
      return visitFluxEntrer(ctx.fluxEntrer());
    } else {
      return visitFluxSortir(ctx.fluxSortir());
    }
  }

  @Override
  public FluxArgent visitFluxEntrer(PatriLangParser.FluxEntrerContext ctx) {
    var compte = findCompteByNom(ctx.MOT().getText());
    var date = visitDate(ctx.date());
    var argentEntrant = visitArgent(ctx.argent());

    return new FluxArgent(
        String.format("Entrer argent dans %s", compte.nom()),
        compte,
        date,
        date,
        date.getDayOfMonth(),
        argentEntrant);
  }

  @Override
  public FluxArgent visitFluxSortir(PatriLangParser.FluxSortirContext ctx) {
    var compte = findCompteByNom(ctx.MOT().getText());
    var date = visitDate(ctx.date());
    var argentSortant = visitArgent(ctx.argent()).mult(-1);

    return new FluxArgent(
        String.format("Sortir argent dans %s", compte.nom()),
        compte,
        date,
        date,
        date.getDayOfMonth(),
        argentSortant);
  }

  @Override
  public Set<Possession> visitPossessions(PatriLangParser.PossessionsContext ctx) {
    Set<Possession> possessions = new HashSet<>();
    for (PatriLangParser.PossessionContext possessionCtx : ctx.possession()) {
      possessions.add(visitPossession(possessionCtx));
    }
    return possessions;
  }

  @Override
  public Possession visitPossession(PatriLangParser.PossessionContext ctx) {
    if (ctx.getText().contains("possède")) {
      return visitMateriel(ctx.materiel());
    }
    return visitFluxArgent(ctx.fluxArgent());
  }

  @Override
  public Materiel visitMateriel(PatriLangParser.MaterielContext ctx) {
    LocalDate instantT = visitDate(ctx.date());
    String nom = ctx.MOT().getText();
    Argent valeurComptable = visitArgent(ctx.argent());
    double tauxDappreciation = visitAppreciation(ctx.appreciation());

    return new Materiel(nom, instantT, instantT, valeurComptable, tauxDappreciation);
  }

  @Override
  public Double visitAppreciation(PatriLangParser.AppreciationContext ctx) {
    int facteurDappreciation = visitAppreciationType(ctx.APPRECIATION_TYPE()).getFacteur();
    return visitDouble(ctx.NUMBER()) / 100 * facteurDappreciation;
  }

  MaterielAppreciationType visitAppreciationType(TerminalNode node) {
    return MaterielAppreciationType.fromString(node.getText());
  }

  Devise visitDevise(TerminalNode node) {
    return switch (node.getText()) {
      case "Ar" -> MGA;
      case "€" -> EUR;
      default -> throw new IllegalArgumentException("Unknown devise: " + node.getText());
    };
  }

  int visitInt(TerminalNode node) {
    return parseInt(node.getText());
  }

  double visitDouble(TerminalNode node) {
    return parseDouble(node.getText());
  }

  private Set<Possession> collectPossessions(PatriLangParser.PatrimoineContext ctx) {
    this.comptes = visitComptes(ctx.comptes());
    Set<Possession> possessions = new HashSet<>(this.comptes);
    possessions.addAll(visitPossessions(ctx.possessions()));
    return possessions;
  }

  private Compte findCompteByNom(String nom) {
    return this.comptes.stream()
        .filter(compte -> compte.nom().equals(nom))
        .findFirst()
        .orElseThrow();
  }
}
