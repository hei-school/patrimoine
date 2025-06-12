package school.hei.patrimoine.patrilang.visitors.possession;

import static java.util.Optional.ofNullable;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class FluxArgentVisitor {
  private final VariableVisitor variableVisitor;

  public FluxArgent apply(FluxArgentEntrerContext ctx) {
    String id = visitText(ctx.id);
    Argent valeurComptable = visitArgent(ctx.valeurComptable);
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);
    Compte compte = this.variableVisitor.asCompte(ctx.compteCrediteurNom);
    var dateFinOpt =
        ofNullable(ctx.dateFin()).map(dateFin -> visitDateFin(dateFin, this.variableVisitor));

    return dateFinOpt
        .map(
            dateFin ->
                new FluxArgent(
                    id, compte, t, dateFin.value(), dateFin.dateOperation(), valeurComptable))
        .orElseGet(() -> new FluxArgent(id, compte, t, valeurComptable));
  }

  public FluxArgent apply(FluxArgentSortirContext ctx) {
    String id = visitText(ctx.id);
    Argent valeurComptable = visitArgent(ctx.valeurComptable).mult(-1);
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);
    Compte compte = this.variableVisitor.asCompte(ctx.compteDebiteurNom);
    var dateFinOpt =
        ofNullable(ctx.dateFin()).map(dateFin -> visitDateFin(dateFin, this.variableVisitor));

    return dateFinOpt
        .map(
            dateFin ->
                new FluxArgent(
                    id, compte, t, dateFin.value(), dateFin.dateOperation(), valeurComptable))
        .orElseGet(() -> new FluxArgent(id, compte, t, valeurComptable));
  }
}
