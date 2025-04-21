package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentTransfererContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.patrilang.modele.DateFin;
import school.hei.patrimoine.patrilang.modele.PossessionGetter;

public class TransferArgentVisitor {
  public TransfertArgent visit(
      FluxArgentTransfererContext ctx, PossessionGetter<Compte> compteGetter) {
    String id = parseNodeValue(ctx.TEXT(0));
    LocalDate t = visitDate(ctx.date());
    Argent valeurComptable = visitArgent(ctx.argent());
    Compte compteDepuis = compteGetter.apply(parseNodeValue(ctx.TEXT(1)));
    Compte compteVers = compteGetter.apply(parseNodeValue(ctx.TEXT(2)));
    DateFin dateFin = ctx.dateFin() == null ? null : visitDateFin(ctx.dateFin());

    if (dateFin != null) {
      return new TransfertArgent(
          id,
          compteDepuis,
          compteVers,
          t,
          dateFin.dateFin(),
          dateFin.dateOperation(),
          valeurComptable);
    }

    return new TransfertArgent(id, compteDepuis, compteVers, t, valeurComptable);
  }
}
