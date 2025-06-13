package school.hei.patrimoine.patrilang.factory;

import school.hei.patrimoine.patrilang.modele.VariableContainer;
import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.possession.*;

public class SectionVisitorFactory {
  public static SectionVisitor make(String casSetFolderPath) {
    var variableContainer = new VariableContainer();
    var variableVisitor = new VariableVisitor(variableContainer);
    var dateVisitor = new DateVisitor(variableVisitor);

    return SectionVisitor.builder()
        .casSetFolderPath(casSetFolderPath)
        .dateVisitor(dateVisitor)
        .variableContainer(variableContainer)
        .variableVisitor(variableVisitor)
        .objectifVisitor(new ObjectifVisitor(variableVisitor, dateVisitor))
        .compteVisitor(new CompteVisitor(dateVisitor))
        .creanceVisitor(new CreanceVisitor(dateVisitor))
        .detteVisitor(new DetteVisitor(dateVisitor))
        .materielVisitor(new MaterielVisitor(dateVisitor))
        .achatMaterielVisitor(new AchatMaterielVisitor(variableVisitor, dateVisitor))
        .fluxArgentVisitor(new FluxArgentVisitor(dateVisitor, variableVisitor))
        .transferArgentVisitor(new TransferArgentVisitor(variableVisitor, dateVisitor))
        .correctionVisitor(new CorrectionVisitor(variableVisitor, dateVisitor))
        .groupPossessionVisitor(new GroupPossessionVisitor(dateVisitor))
        .build();
  }
}
