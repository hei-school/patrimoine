package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.mapper.DeviseMapper.stringToDevise;
import static school.hei.patrimoine.patrilang.mapper.MaterielAppreciationMapper.stringToMaterielAppreciationType;

import org.antlr.v4.runtime.tree.TerminalNode;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.PersonneMorale;
import school.hei.patrimoine.patrilang.modele.DateFin;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class BaseVisitor {
  private static final int MAX_MONTH_DATE_OPERATION = 31;

  public static Personne visitPersonne(TextContext ctx) {
    return new Personne(parseNodeValue(ctx.TEXT()));
  }

  public static PersonneMorale visitPersonneMorale(TextContext ctx) {
    return new PersonneMorale(parseNodeValue(ctx.TEXT()));
  }

  public static DateFin visitDateFin(DateFinContext ctx, VariableVisitor variableVisitor) {
    int dateDOpération =
        ctx.dateFinOperation().MOT_FIN_DU_MOIS() != null
            ? MAX_MONTH_DATE_OPERATION
            : variableVisitor.asInt(ctx.dateFinOperation().variable());
    var dateFinValue = variableVisitor.asDate(ctx.dateValue);

    return new DateFin(dateDOpération, dateFinValue);
  }

  public static Devise visitDevise(DeviseContext ctx) {
    return stringToDevise(parseNodeValue(ctx.DEVISE()));
  }

  public static double visitMaterielAppreciationFacteur(TerminalNode ctx) {
    return stringToMaterielAppreciationType(ctx.getText()).getFacteur();
  }

  public static String visitText(TextContext ctx) {
    return parseNodeValue(ctx.TEXT());
  }

  public static String parseNodeValue(TerminalNode node) {
    return node.getText();
  }
}
