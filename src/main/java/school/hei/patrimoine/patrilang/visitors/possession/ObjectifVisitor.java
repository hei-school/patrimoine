package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ObjectifContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitArgent;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.objectif.Objectif;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class ObjectifVisitor implements SimpleVisitor<ObjectifContext, Objectif> {
  private final VariableVisitor<DateContext, LocalDate> variableDateVisitor;
  private final VariableVisitor<CompteContext, Compte> variableCompteVisitor;

  @Override
  public Objectif apply(ObjectifContext ctx) {
    Argent valeurComptable = visitArgent(ctx.valeurComptable);
    LocalDate t = this.variableDateVisitor.apply(ctx.dateValue);
    Compte compte = this.variableCompteVisitor.apply(ctx.compteNom);

    return new Objectif(compte, t, valeurComptable);
  }
}
