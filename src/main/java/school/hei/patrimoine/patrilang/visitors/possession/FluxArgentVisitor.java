package school.hei.patrimoine.patrilang.visitors.possession;

import static java.util.Optional.ofNullable;
import static school.hei.patrimoine.modele.possession.TypeFEC.*;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.TerminalNode;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.TypeFEC;
import school.hei.patrimoine.patrilang.modele.DateFin;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class FluxArgentVisitor {
  private final VariableVisitor variableVisitor;
  private final IdVisitor idVisitor;

  public FluxArgent apply(FluxArgentEntrerContext ctx) {
    String id = idVisitor.apply(ctx.id());
    Argent valeurComptable = variableVisitor.asArgent(ctx.valeurComptable);
    LocalDate dateOperation = variableVisitor.asDate(ctx.dateValue);
    Compte compte = variableVisitor.asCompte(ctx.compteCrediteurNom);
    TypeFEC typeFEC = toTypeFEC(ctx.id().TYPE_FEC());

    checkPositive(valeurComptable, id, true);

    DateFin dateFin =
        ofNullable(ctx.dateFin()).map(df -> visitDateFin(df, variableVisitor)).orElse(null);

    return buildFlux(id, compte, dateOperation, valeurComptable, typeFEC, dateFin);
  }

  public FluxArgent apply(FluxArgentSortirContext ctx) {
    String id = idVisitor.apply(ctx.id());
    Argent valeurComptable = variableVisitor.asArgent(ctx.valeurComptable).mult(-1);
    LocalDate dateOperation = variableVisitor.asDate(ctx.dateValue);
    Compte compte = variableVisitor.asCompte(ctx.compteDebiteurNom);
    TypeFEC typeFEC = toTypeFEC(ctx.id().TYPE_FEC());

    checkPositive(valeurComptable, id, false);

    DateFin dateFin =
        ofNullable(ctx.dateFin()).map(df -> visitDateFin(df, variableVisitor)).orElse(null);

    return buildFlux(id, compte, dateOperation, valeurComptable, typeFEC, dateFin);
  }

  private void checkPositive(Argent valeur, String id, boolean entree) {
    if (entree && valeur.lt(0)) {
      throw new IllegalArgumentException(
          "Entrer " + valeur + " pour l'opération " + id + " n'est pas possible, valeur < 0");
    }
    if (!entree && valeur.gt(0)) {
      throw new IllegalArgumentException(
          "Sortir " + valeur + " pour l'opération " + id + " n'est pas possible, valeur > 0");
    }
  }

  private FluxArgent buildFlux(
      String id,
      Compte compte,
      LocalDate dateOperation,
      Argent valeurComptable,
      TypeFEC typeFEC,
      DateFin dateFin) {

    if (dateFin != null) {
      return typeFEC == null
          ? new FluxArgent(
              id, compte, dateOperation, dateFin.value(), dateFin.dateOperation(), valeurComptable)
          : new FluxArgent(
              id,
              compte,
              dateOperation,
              dateFin.value(),
              dateFin.dateOperation(),
              valeurComptable,
              typeFEC);
    }
    return typeFEC == null
        ? new FluxArgent(id, compte, dateOperation, valeurComptable)
        : new FluxArgent(id, compte, dateOperation, valeurComptable, typeFEC);
  }

  private static TypeFEC toTypeFEC(TerminalNode node) {
    if (node == null) return null;
    return switch (node.getText().toUpperCase()) {
      case "IMMOBILISATION", "IMMO" -> IMMOBILISATION;
      case "CHARGE", "CHG" -> CHARGE;
      case "PRODUIT", "PRD" -> PRODUIT;
      case "CCA" -> CCA;
      case "AUTRE" -> AUTRE;
      default -> throw new IllegalArgumentException("TypeFEC inconnu : " + node.getText());
    };
  }
}
