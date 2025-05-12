package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentTransfererContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariable;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.patrilang.modele.PossessionGetter;

public class TransferArgentVisitor {
  public TransfertArgent visit(
      FluxArgentTransfererContext ctx, PossessionGetter<Compte> compteGetter) {
    String id = visitVariable(ctx.variable(0), TextContext.class, BaseVisitor::visitText);
    LocalDate t = visitVariable(ctx.variable(1), DateContext.class, BaseVisitor::visitDate);
    Argent valeurComptable =
        visitVariable(ctx.variable(2), ArgentContext.class, BaseVisitor::visitArgent);
    Compte débiteur =
        compteGetter.apply(
            visitVariable(ctx.variable(3), TextContext.class, BaseVisitor::visitText));
    Compte créditeur =
        compteGetter.apply(
            visitVariable(ctx.variable(4), TextContext.class, BaseVisitor::visitText));
    var dateFin = isNull(ctx.dateFin()) ? null : visitDateFin(ctx.dateFin());

    if (dateFin != null) {
      return new TransfertArgent(
          id, débiteur, créditeur, t, dateFin.dateFin(), dateFin.dateOperation(), valeurComptable);
    }

    return new TransfertArgent(id, débiteur, créditeur, t, valeurComptable);
  }
}
