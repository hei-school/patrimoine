package school.hei.patrimoine.patrilang.visitors.possession;

import static java.util.Optional.ofNullable;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.TerminalNode;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.comptable.TypeComptable;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class FluxArgentVisitor {
  private final VariableVisitor variableVisitor;
  private final IdVisitor idVisitor;

  public OperationComptable apply(FluxArgentEntrerContext ctx) {
    String id = this.idVisitor.apply(ctx.id());
    Argent valeurComptable = this.variableVisitor.asArgent(ctx.valeurComptable);
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);
    Compte compte = this.variableVisitor.asCompte(ctx.compteCrediteurNom);
    TypeComptable typeExplicite = toTypeComptable(ctx.id().TYPE_COMPTABLE());
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

    var flux =
        dateFinOpt
            .map(
                dateFin ->
                    new FluxArgent(
                        id, compte, t, dateFin.value(), dateFin.dateOperation(), valeurComptable))
            .orElseGet(() -> new FluxArgent(id, compte, t, valeurComptable));

    var type = addTypeComptableIfNotNull(typeExplicite, flux);

    return new OperationComptable(flux, type);
  }

  public OperationComptable apply(FluxArgentSortirContext ctx) {
    String id = this.idVisitor.apply(ctx.id());
    Argent valeurComptable = this.variableVisitor.asArgent(ctx.valeurComptable).mult(-1);
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);
    Compte compte = this.variableVisitor.asCompte(ctx.compteDebiteurNom);
    TypeComptable typeExplicite = toTypeComptable(ctx.id().TYPE_COMPTABLE());
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

    var flux =
        dateFinOpt
            .map(
                dateFin ->
                    new FluxArgent(
                        id, compte, t, dateFin.value(), dateFin.dateOperation(), valeurComptable))
            .orElseGet(() -> new FluxArgent(id, compte, t, valeurComptable));

    var type = addTypeComptableIfNotNull(typeExplicite, flux);

    return new OperationComptable(flux, type);
  }

  private static TypeComptable addTypeComptableIfNotNull(TypeComptable type, FluxArgent flux) {
    if (type != null) {
      return type;
    }
    return OperationComptable.make(flux).type();
  }

  protected static TypeComptable toTypeComptable(TerminalNode node) {
    return switch (node.getText().toUpperCase()) {
      case "IMMOBILISATION", "IMMO" -> IMMOBILISATION;
      case "CHARGE", "CHG" -> CHARGE;
      case "PRODUIT", "PRD" -> PRODUIT;
      case "CCA" -> CCA;
      case "AUTRE" -> AUTRE;
      default -> throw new IllegalArgumentException("Type Comptable inconnu : " + node.getText());
    };
  }
}
