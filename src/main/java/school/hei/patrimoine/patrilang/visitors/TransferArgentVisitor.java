package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentTransfererContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.patrilang.modele.PossessionGetter;

public class TransferArgentVisitor {
  public TransfertArgent visit(
      FluxArgentTransfererContext ctx, PossessionGetter<Compte> compteGetter) {
    String id = visitVariableAsText(ctx.variable(0));
    LocalDate t = visitVariableAsDate(ctx.variable(1));
    Argent valeurComptable = visitVariableAsArgent(ctx.variable(2));
    Compte débiteur = compteGetter.apply(visitVariableAsText(ctx.variable(3)));
    Compte créditeur = compteGetter.apply(visitVariableAsText(ctx.variable(4)));
    var dateFin = isNull(ctx.dateFin()) ? null : visitDateFin(ctx.dateFin());

    if (dateFin != null) {
      return new TransfertArgent(
          id, débiteur, créditeur, t, dateFin.dateFin(), dateFin.dateOperation(), valeurComptable);
    }

    return new TransfertArgent(id, débiteur, créditeur, t, valeurComptable);
  }
}
