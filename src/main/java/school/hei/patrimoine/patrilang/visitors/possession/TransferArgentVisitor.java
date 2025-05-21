package school.hei.patrimoine.patrilang.visitors.possession;

import static java.util.Objects.isNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentTransfererContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.patrilang.modele.DateFin;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class TransferArgentVisitor
    implements SimplePossessionVisitor<FluxArgentTransfererContext, TransfertArgent> {
  private final VariableVisitor<DateContext, LocalDate> dateVisitor;
  private final VariableVisitor<CompteContext, Compte> compteVisitor;

  public TransfertArgent apply(FluxArgentTransfererContext ctx) {
    String id = visitVariableAsText(ctx.variable(0));
    LocalDate t = this.dateVisitor.apply(ctx.variable(1));
    Argent valeurComptable = visitVariableAsArgent(ctx.variable(2));
    Compte débiteur = this.compteVisitor.apply(ctx.variable(3));
    Compte créditeur = this.compteVisitor.apply(ctx.variable(4));
    DateFin dateFin = isNull(ctx.dateFin()) ? null : visitDateFin(ctx.dateFin(), this.dateVisitor);

    if (!isNull(dateFin)) {
      return new TransfertArgent(
          id, débiteur, créditeur, t, dateFin.dateFin(), dateFin.dateOperation(), valeurComptable);
    }

    return new TransfertArgent(id, débiteur, créditeur, t, valeurComptable);
  }
}
