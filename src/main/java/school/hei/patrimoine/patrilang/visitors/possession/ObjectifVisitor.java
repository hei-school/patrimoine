package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ObjectifContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitArgent;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.objectif.Objectif;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.visitors.DateVisitor;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class ObjectifVisitor implements SimpleVisitor<ObjectifContext, Objectif> {
  private final VariableVisitor variableVisitor;
  private final DateVisitor dateVisitor;

  @Override
  public Objectif apply(ObjectifContext ctx) {
    Argent valeurComptable = visitArgent(ctx.valeurComptable);
    LocalDate t = this.dateVisitor.apply(ctx.dateValue);
    Compte compte = this.variableVisitor.asCompte(ctx.compteNom);

    return new Objectif(compte, t, valeurComptable);
  }
}
