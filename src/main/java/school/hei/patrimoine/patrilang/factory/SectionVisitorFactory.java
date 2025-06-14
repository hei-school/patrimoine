package school.hei.patrimoine.patrilang.factory;

import school.hei.patrimoine.patrilang.modele.VariableContainer;
import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.possession.*;

public class SectionVisitorFactory {
  public static SectionVisitor make(String casSetFolderPath) {

    var variableContainer = new VariableContainer();
    var variableVisitor = new VariableVisitor(variableContainer);
    var idVisitor = new IdVisitor(variableVisitor);
    var expressionVisitor = new ExpressionVisitor();
    var dateVisitor = new DateVisitor(variableVisitor);
    var argentVisitor = new ArgentVisitor(expressionVisitor);

    return SectionVisitor.builder()
        .expressionVisitor(expressionVisitor)
        .argentVisitor(argentVisitor)
        .casSetFolderPath(casSetFolderPath)
        .dateVisitor(dateVisitor)
        .variableContainer(variableContainer)
        .variableVisitor(variableVisitor)
        .objectifVisitor(new ObjectifVisitor(variableVisitor, dateVisitor, argentVisitor))
        .compteVisitor(new CompteVisitor(dateVisitor, argentVisitor))
        .creanceVisitor(new CreanceVisitor(dateVisitor, argentVisitor))
        .detteVisitor(new DetteVisitor(dateVisitor, argentVisitor))
        .materielVisitor(new MaterielVisitor(dateVisitor, argentVisitor))
        .achatMaterielVisitor(new AchatMaterielVisitor(variableVisitor, dateVisitor, argentVisitor))
        .fluxArgentVisitor(
            new FluxArgentVisitor(dateVisitor, variableVisitor, argentVisitor, idVisitor))
        .transferArgentVisitor(
            new TransferArgentVisitor(variableVisitor, dateVisitor, argentVisitor, idVisitor))
        .correctionVisitor(
            new CorrectionVisitor(variableVisitor, dateVisitor, argentVisitor, idVisitor))
        .groupPossessionVisitor(new GroupPossessionVisitor(dateVisitor))
        .build();
  }
}
