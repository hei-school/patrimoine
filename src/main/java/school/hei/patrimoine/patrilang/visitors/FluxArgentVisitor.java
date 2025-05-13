package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.modele.DateFin;
import school.hei.patrimoine.patrilang.modele.PossessionGetter;

@RequiredArgsConstructor
public class FluxArgentVisitor {
  public FluxArgent visit(FluxArgentEntrerContext ctx, PossessionGetter<Compte> compteGetter) {
    String id = visitVariableAsText(ctx.variable(0));
    LocalDate t = visitVariableAsDate(ctx.variable(1));
    Argent valeurComptable = visitVariableAsArgent(ctx.variable(2));
    Compte compte = compteGetter.apply(visitVariableAsText(ctx.variable(3)));
    DateFin dateFin = ctx.dateFin() == null ? null : visitDateFin(ctx.dateFin());

    if (dateFin != null) {
      return new FluxArgent(
          id, compte, t, dateFin.dateFin(), dateFin.dateOperation(), valeurComptable);
    }

    return new FluxArgent(id, compte, t, valeurComptable);
  }

  public FluxArgent visit(FluxArgentSortirContext ctx, PossessionGetter<Compte> compteGetter) {
    String id = visitVariableAsText(ctx.variable(0));
    LocalDate t = visitVariableAsDate(ctx.variable(1));
    Argent valeurComptable = visitVariableAsArgent(ctx.variable(2));
    Compte compte = compteGetter.apply(visitVariableAsText(ctx.variable(3)));
    DateFin dateFin = ctx.dateFin() == null ? null : visitDateFin(ctx.dateFin());

    if (dateFin != null) {
      return new FluxArgent(
          id, compte, t, dateFin.dateFin(), dateFin.dateOperation(), valeurComptable.mult(-1));
    }

    return new FluxArgent(id, compte, t, valeurComptable.mult(-1));
  }
}
