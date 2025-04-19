package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentTransfererContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;

@RequiredArgsConstructor
public class TransferArgentVisitor
    implements PossessionVisitor<TransfertArgent, FluxArgentTransfererContext> {
  private final Function<String, Compte> compteGetter;

  @Override
  public TransfertArgent visit(FluxArgentTransfererContext ctx) {
    String id = parseNodeValue(ctx.TEXT(0));
    LocalDate t = visitDate(ctx.date());
    Argent valeurComptable = visitArgent(ctx.argent());
    Compte compteDepuis = compteGetter.apply(parseNodeValue(ctx.TEXT(1)));
    Compte compteVers = compteGetter.apply(parseNodeValue(ctx.TEXT(2)));

    return new TransfertArgent(id, compteDepuis, compteVers, t, valeurComptable);
  }
}
