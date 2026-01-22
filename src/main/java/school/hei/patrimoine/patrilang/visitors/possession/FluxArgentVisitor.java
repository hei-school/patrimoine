package school.hei.patrimoine.patrilang.visitors.possession;

import static java.util.Optional.ofNullable;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.TerminalNode;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.TypeFEC;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class FluxArgentVisitor {
  private final VariableVisitor variableVisitor;
  private final IdVisitor idVisitor;

  public FluxArgent apply(FluxArgentEntrerContext ctx) {
    String id = this.idVisitor.apply(ctx.id());
    Argent valeurComptable = this.variableVisitor.asArgent(ctx.valeurComptable);
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);
    Compte compte = this.variableVisitor.asCompte(ctx.compteCrediteurNom);
    TypeFEC typeFEC = toTypeFEC(ctx.id().TYPE_FEC());

    var dateFinOpt =
        ofNullable(ctx.dateFin()).map(dateFin -> visitDateFin(dateFin, this.variableVisitor));

    if (valeurComptable.lt(0)) {
      throw new IllegalArgumentException(
          "Entrer "
              + valeurComptable
              + " pour l'opération "
              + id
              + " n'est pas possible, la valeur est inférieur à 0");
    }

    return dateFinOpt
        .map(
            df ->
                typeFEC == null
                    ? new FluxArgent(id, compte, t, df.value(), df.dateOperation(), valeurComptable)
                    : new FluxArgent(
                        id, compte, t, df.value(), df.dateOperation(), valeurComptable, typeFEC))
        .orElseGet(
            () ->
                typeFEC == null
                    ? new FluxArgent(id, compte, t, valeurComptable)
                    : new FluxArgent(id, compte, t, valeurComptable, typeFEC));
  }

  public FluxArgent apply(FluxArgentSortirContext ctx) {
    String id = this.idVisitor.apply(ctx.id());
    Argent valeurComptable = this.variableVisitor.asArgent(ctx.valeurComptable).mult(-1);
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);
    Compte compte = this.variableVisitor.asCompte(ctx.compteDebiteurNom);

    TypeFEC typeFEC = toTypeFEC(ctx.id().TYPE_FEC());

    var dateFinOpt =
        ofNullable(ctx.dateFin()).map(dateFin -> visitDateFin(dateFin, this.variableVisitor));

    if (valeurComptable.gt(0)) {
      throw new IllegalArgumentException(
          "Sortir "
              + valeurComptable
              + " pour l'opération "
              + id
              + " n'est pas possible, la valeur est supérieur à 0");
    }

    return dateFinOpt
        .map(
            dateFin ->
                typeFEC == null
                    ? new FluxArgent(
                        id, compte, t, dateFin.value(), dateFin.dateOperation(), valeurComptable)
                    : new FluxArgent(
                        id,
                        compte,
                        t,
                        dateFin.value(),
                        dateFin.dateOperation(),
                        valeurComptable,
                        typeFEC))
        .orElseGet(
            () ->
                typeFEC == null
                    ? new FluxArgent(id, compte, t, valeurComptable)
                    : new FluxArgent(id, compte, t, valeurComptable, typeFEC));
  }

  private static TypeFEC toTypeFEC(TerminalNode node) {
    if (node == null) {
      return null;
    }
    return switch (node.getText().toUpperCase()) {
      case "IMMOBILISATION", "IMMO" -> TypeFEC.IMMOBILISATION;
      case "CHARGE", "CHG" -> TypeFEC.CHARGE;
      case "PRODUIT", "PRD" -> TypeFEC.PRODUIT;
      case "CCA" -> TypeFEC.CCA;
      default -> throw new IllegalArgumentException("TypeFEC inconnu : " + node.getText());
    };
  }
}
