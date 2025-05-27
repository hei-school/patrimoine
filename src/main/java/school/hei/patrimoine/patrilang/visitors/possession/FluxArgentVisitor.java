package school.hei.patrimoine.patrilang.visitors.possession;

import static java.util.Optional.ofNullable;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsText;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.modele.DateFin;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class FluxArgentVisitor {
  private final VariableVisitor<DateContext, LocalDate> variableDateVisitor;
  private final VariableVisitor<CompteContext, Compte> variableCompteVisitor;

  public FluxArgent apply(FluxArgentEntrerContext ctx) {
    String id = visitVariableAsText(ctx.id);
    Argent valeurComptable = visitArgent(ctx.valeurComptable);
    Optional<DateFin> dateFinOpt =
        ofNullable(ctx.dateFin()).map(dateFin -> visitDateFin(dateFin, variableDateVisitor));
    LocalDate t = variableDateVisitor.apply(ctx.dateValue);
    Compte compte = variableCompteVisitor.apply(ctx.compteCrediteurNom);

    return dateFinOpt
        .map(
            dateFin ->
                new FluxArgent(
                    id, compte, t, dateFin.value(), dateFin.dateOperation(), valeurComptable))
        .orElseGet(() -> new FluxArgent(id, compte, t, valeurComptable));
  }

  public FluxArgent apply(FluxArgentSortirContext ctx) {
    String id = visitVariableAsText(ctx.id);
    Argent valeurComptable = visitArgent(ctx.valeurComptable).mult(-1);
    Optional<DateFin> dateFinOpt =
        ofNullable(ctx.dateFin()).map(dateFin -> visitDateFin(dateFin, variableDateVisitor));
    LocalDate t = variableDateVisitor.apply(ctx.dateValue);
    Compte compte = variableCompteVisitor.apply(ctx.compteDebiteurNom);

    return dateFinOpt
        .map(
            dateFin ->
                new FluxArgent(
                    id, compte, t, dateFin.value(), dateFin.dateOperation(), valeurComptable))
        .orElseGet(() -> new FluxArgent(id, compte, t, valeurComptable));
  }
}
