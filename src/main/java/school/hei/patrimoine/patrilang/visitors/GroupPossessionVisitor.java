package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DateContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.SousTitreContext;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Possession;

@RequiredArgsConstructor
public class GroupPossessionVisitor
    implements BiFunction<SousTitreContext, Set<Possession>, GroupePossession> {
  private final VariableVisitor<DateContext, LocalDate> variableDateVisitor;

  @Override
  public GroupePossession apply(SousTitreContext ctx, Set<Possession> possessions) {
    String nom = visitVariableAsText(ctx.variable(0));
    LocalDate t = this.variableDateVisitor.apply(ctx.variable(1));
    Devise devise = visitVariableAsDevise(ctx.variable(2));

    return new GroupePossession(nom, devise, t, possessions);
  }
}
