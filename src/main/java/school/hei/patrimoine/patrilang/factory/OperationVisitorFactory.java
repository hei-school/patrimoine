package school.hei.patrimoine.patrilang.factory;

import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.possession.*;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class OperationVisitorFactory {
  public static OperationVisitor make(
      VariableVisitor variableVisitor, IdVisitor idVisitor, ArgentVisitor argentVisitor) {
    return OperationVisitor.builder()
        .groupPossessionVisitor(new GroupPossessionVisitor(variableVisitor))
        .achatMaterielVisitor(new AchatMaterielVisitor(variableVisitor, argentVisitor))
        .correctionVisitor(new CorrectionVisitor(variableVisitor, argentVisitor, idVisitor))
        .materielVisitor(new MaterielVisitor(variableVisitor, argentVisitor))
        .objectifVisitor(new ObjectifVisitor(variableVisitor, argentVisitor))
        .transferArgentVisitor(new TransferArgentVisitor(variableVisitor, argentVisitor, idVisitor))
        .fluxArgentVisitor(new FluxArgentVisitor(variableVisitor, argentVisitor, idVisitor))
        .operationTemplateCallVisitor(new OperationTemplateCallVisitor(variableVisitor))
        .build();
  }

  public static OperationVisitor make(VariableVisitor variableVisitor) {
    var idVisitor = new IdVisitor(variableVisitor);
    var argentVisitor = new ArgentVisitor(new ExpressionVisitor(variableVisitor));

    return make(variableVisitor, idVisitor, argentVisitor);
  }
}
