package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariable;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.modele.DateFin;
import school.hei.patrimoine.patrilang.modele.PossessionGetter;

@RequiredArgsConstructor
public class FluxArgentVisitor {
  public FluxArgent visit(FluxArgentEntrerContext ctx, PossessionGetter<Compte> compteGetter) {
    String id = visitVariable(ctx.variable(0), TextContext.class, BaseVisitor::visitText);
    LocalDate t = visitVariable(ctx.variable(1), DateContext.class, BaseVisitor::visitDate);
    Argent valeurComptable =
        visitVariable(ctx.variable(2), ArgentContext.class, BaseVisitor::visitArgent);
    Compte compte =
        compteGetter.apply(
            visitVariable(ctx.variable(3), TextContext.class, BaseVisitor::visitText));
    DateFin dateFin = ctx.dateFin() == null ? null : visitDateFin(ctx.dateFin());

    if (dateFin != null) {
      return new FluxArgent(
          id, compte, t, dateFin.dateFin(), dateFin.dateOperation(), valeurComptable);
    }

    return new FluxArgent(id, compte, t, valeurComptable);
  }

  public FluxArgent visit(FluxArgentSortirContext ctx, PossessionGetter<Compte> compteGetter) {
    String id = visitVariable(ctx.variable(0), TextContext.class, BaseVisitor::visitText);
    LocalDate t = visitVariable(ctx.variable(1), DateContext.class, BaseVisitor::visitDate);
    Argent valeurComptable =
        visitVariable(ctx.variable(2), ArgentContext.class, BaseVisitor::visitArgent);
    Compte compte =
        compteGetter.apply(
            visitVariable(ctx.variable(3), TextContext.class, BaseVisitor::visitText));
    DateFin dateFin = ctx.dateFin() == null ? null : visitDateFin(ctx.dateFin());

    if (dateFin != null) {
      return new FluxArgent(
          id, compte, t, dateFin.dateFin(), dateFin.dateOperation(), valeurComptable.mult(-1));
    }

    return new FluxArgent(id, compte, t, valeurComptable.mult(-1));
  }
}
