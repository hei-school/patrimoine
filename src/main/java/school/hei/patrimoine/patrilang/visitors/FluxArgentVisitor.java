package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentEntrerContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentSortirContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

@RequiredArgsConstructor
public class FluxArgentVisitor {
  private final Function<String, Compte> compteGetter;

  public FluxArgent visit(FluxArgentEntrerContext ctx) {
    String id = parseNodeValue(ctx.TEXT(0));
    Compte compte = compteGetter.apply(parseNodeValue(ctx.TEXT(1)));
    LocalDate t = visitDate(ctx.date());
    Argent valeurComptable = visitArgent(ctx.argent());

    return new FluxArgent(id, compte, t, valeurComptable);
  }

  public FluxArgent visit(FluxArgentSortirContext ctx) {
    String id = parseNodeValue(ctx.TEXT(0));
    Compte compte = compteGetter.apply(parseNodeValue(ctx.TEXT(1)));
    LocalDate t = visitDate(ctx.date());
    Argent valeurComptable = visitArgent(ctx.argent());

    return new FluxArgent(id, compte, t, valeurComptable.mult(-1));
  }
}
