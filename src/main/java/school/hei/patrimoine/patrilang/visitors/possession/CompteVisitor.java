package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitText;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class CompteVisitor implements SimpleVisitor<CompteContext, Compte> {
  private final VariableVisitor variableVisitor;
  private final ArgentVisitor argentVisitor;

  @Override
  public Compte apply(CompteContext ctx) {
    String nom = visitText(ctx.nom);
    Argent valeurComptable = this.argentVisitor.apply(ctx.valeurComptable);
    LocalDate t = this.variableVisitor.asDate(ctx.dateValue);

    return new Compte(nom, t, valeurComptable);
  }
}
