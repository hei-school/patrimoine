package school.hei.patrimoine.patrilang.visitors.possession;

import static java.util.Optional.ofNullable;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentTransfererContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class TransferArgentVisitor
    implements SimpleVisitor<FluxArgentTransfererContext, TransfertArgent> {
  private final VariableVisitor variableVisitor;
  private final ArgentVisitor argentVisitor;
  private final IdVisitor idVisitor;

  public TransfertArgent apply(FluxArgentTransfererContext ctx) {
    String id = this.idVisitor.apply(ctx.id());
    Argent valeurComptable = this.argentVisitor.apply(ctx.valeurComptable);
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);
    Compte débiteur = this.variableVisitor.asCompte(ctx.compteDebiteurNom);
    Compte créditeur = this.variableVisitor.asCompte(ctx.compteCrediteurNom);
    var dateFinOpt =
        ofNullable(ctx.dateFin()).map(context -> visitDateFin(context, this.variableVisitor));

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
