package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;
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
    return createFluxArgent(ctx, 1);
  }

  public FluxArgent apply(FluxArgentSortirContext ctx) {
    return createFluxArgent(ctx, -1);
  }

  private FluxArgent createFluxArgent(ParserRuleContext ctx, int facteur) {
    String id = visitVariableAsText(invokeVariable(ctx, 0));
    LocalDate t = variableDateVisitor.apply(invokeVariable(ctx, 1));
    Argent valeurComptable = visitVariableAsArgent(invokeVariable(ctx, 2)).mult(facteur);
    Compte compte = variableCompteVisitor.apply(invokeVariable(ctx, 3));
    Optional<DateFin> dateFinOpt =
        invokeDateFin(ctx).map(dateFin -> visitDateFin(dateFin, variableDateVisitor));

    return dateFinOpt
        .map(
            dateFin ->
                new FluxArgent(
                    id, compte, t, dateFin.value(), dateFin.dateOperation(), valeurComptable))
        .orElseGet(() -> new FluxArgent(id, compte, t, valeurComptable));
  }

  private VariableContext invokeVariable(ParserRuleContext ctx, int index) {
    return switch (ctx) {
      case FluxArgentEntrerContext entrerCtx -> entrerCtx.variable(index);
      case FluxArgentSortirContext sortirCtx -> sortirCtx.variable(index);
      default ->
          throw new IllegalArgumentException(
              "Contexte non supporté : " + ctx.getClass().getSimpleName());
    };
  }

  private Optional<DateFinContext> invokeDateFin(ParserRuleContext ctx) {
    return switch (ctx) {
      case FluxArgentEntrerContext entrerCtx -> Optional.ofNullable(entrerCtx.dateFin());
      case FluxArgentSortirContext sortirCtx -> Optional.ofNullable(sortirCtx.dateFin());
      default ->
          throw new IllegalArgumentException(
              "Contexte non supporté : " + ctx.getClass().getSimpleName());
    };
  }
}
