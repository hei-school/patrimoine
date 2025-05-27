package school.hei.patrimoine.patrilang.visitors.possession;

import static java.util.Optional.ofNullable;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentTransfererContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.patrilang.modele.DateFin;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class TransferArgentVisitor
    implements SimpleVisitor<FluxArgentTransfererContext, TransfertArgent> {
  private final VariableVisitor<DateContext, LocalDate> dateVisitor;
  private final VariableVisitor<CompteContext, Compte> compteVisitor;

  public TransfertArgent apply(FluxArgentTransfererContext ctx) {
    String id = visitVariableAsText(ctx.id);
    Argent valeurComptable = visitArgent(ctx.valeurComptable);
    Optional<DateFin> dateFinOpt =
        ofNullable(ctx.dateFin()).map(context -> visitDateFin(context, this.dateVisitor));
    LocalDate t = this.dateVisitor.apply(ctx.dateValue);
    Compte débiteur = this.compteVisitor.apply(ctx.compteDebiteurNom);
    Compte créditeur = this.compteVisitor.apply(ctx.compteCrediteurNom);

    return dateFinOpt
        .map(
            dateFin ->
                new TransfertArgent(
                    id,
                    débiteur,
                    créditeur,
                    t,
                    dateFin.value(),
                    dateFin.dateOperation(),
                    valeurComptable))
        .orElseGet(() -> new TransfertArgent(id, débiteur, créditeur, t, valeurComptable));
  }
}
