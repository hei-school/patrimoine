package school.hei.patrimoine.patrilang.factory;

import school.hei.patrimoine.patrilang.modele.VariableContainer;
import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.possession.*;

public class SectionVisitorFactory {
  public static SectionVisitor make(String casSetFolderPath) {
    var variableContainer = new VariableContainer();
    var variableVisitor = new VariableVisitor(variableContainer);

    return SectionVisitor.builder()
        .casSetFolderPath(casSetFolderPath)
        .variableContainer(variableContainer)
        .variableVisitor(variableVisitor)
        .objectifVisitor(new ObjectifVisitor(variableVisitor))
        .compteVisitor(new CompteVisitor(variableVisitor))
        .creanceVisitor(new CreanceVisitor(variableVisitor))
        .detteVisitor(new DetteVisitor(variableVisitor))
        .materielVisitor(new MaterielVisitor(variableVisitor))
        .achatMaterielVisitor(new AchatMaterielVisitor(variableVisitor))
        .fluxArgentVisitor(new FluxArgentVisitor(variableVisitor))
        .transferArgentVisitor(new TransferArgentVisitor(variableVisitor))
        .correctionVisitor(new CorrectionVisitor(variableVisitor))
        .groupPossessionVisitor(new GroupPossessionVisitor(variableVisitor))
        .build();
  }
}
