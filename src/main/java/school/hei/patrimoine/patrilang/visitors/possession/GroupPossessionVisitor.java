package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.SousTitreContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitDevise;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsText;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

@RequiredArgsConstructor
public class GroupPossessionVisitor
    implements BiFunction<SousTitreContext, Set<Possession>, GroupePossession> {
  private final VariableVisitor<DateContext, LocalDate> variableDateVisitor;

  @Override
  public GroupePossession apply(SousTitreContext ctx, Set<Possession> possessions) {
    String nom = visitVariableAsText(ctx.nom);
    Devise devise = visitDevise(ctx.devise());
    LocalDate t = this.variableDateVisitor.apply(ctx.dateValue);

    return new GroupePossession(nom, devise, t, possessions);
  }
}
