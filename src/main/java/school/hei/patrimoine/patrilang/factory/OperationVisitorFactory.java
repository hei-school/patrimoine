package school.hei.patrimoine.patrilang.factory;

import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.possession.*;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class OperationVisitorFactory {
  public static OperationVisitor make(VariableVisitor variableVisitor, IdVisitor idVisitor) {
    return OperationVisitor.builder()
        .groupPossessionVisitor(new GroupPossessionVisitor(variableVisitor))
        .achatMaterielVisitor(new AchatMaterielVisitor(variableVisitor))
        .correctionVisitor(new CorrectionVisitor(variableVisitor, idVisitor))
        .materielVisitor(new MaterielVisitor(variableVisitor))
        .objectifVisitor(new ObjectifVisitor(variableVisitor))
        .transferArgentVisitor(new TransferArgentVisitor(variableVisitor, idVisitor))
        .fluxArgentVisitor(new FluxArgentVisitor(variableVisitor, idVisitor))
        .operationTemplateCallVisitor(new OperationTemplateCallVisitor(variableVisitor))
        .build();
  }

  public static OperationVisitor make(VariableVisitor variableVisitor) {
    return make(variableVisitor, new IdVisitor(variableVisitor));
  }
}
