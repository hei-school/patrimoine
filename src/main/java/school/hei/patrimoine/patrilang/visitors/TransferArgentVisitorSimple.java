package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentTransfererContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.patrilang.modele.PossessionGetter;

@RequiredArgsConstructor
public class TransferArgentVisitorSimple
    implements SimplePossessionVisitor<TransfertArgent, FluxArgentTransfererContext> {
  private final PossessionGetter<Compte> compteGetter;

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
