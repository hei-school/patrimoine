package school.hei.patrimoine.patrilang.factory;

import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.possession.*;

public class OperationVisitorFactory {
  public static OperationVisitor make(
      VariableVisitor variableVisitor,
      IdVisitor idVisitor,
      ArgentVisitor argentVisitor,
      DateVisitor dateVisitor) {
    return OperationVisitor.builder()
        .groupPossessionVisitor(new GroupPossessionVisitor(dateVisitor))
        .achatMaterielVisitor(new AchatMaterielVisitor(variableVisitor, dateVisitor, argentVisitor))
        .correctionVisitor(
            new CorrectionVisitor(variableVisitor, dateVisitor, argentVisitor, idVisitor))
        .materielVisitor(new MaterielVisitor(dateVisitor, argentVisitor))
        .objectifVisitor(new ObjectifVisitor(variableVisitor, dateVisitor, argentVisitor))
        .transferArgentVisitor(
            new TransferArgentVisitor(variableVisitor, dateVisitor, argentVisitor, idVisitor))
        .fluxArgentVisitor(
            new FluxArgentVisitor(dateVisitor, variableVisitor, argentVisitor, idVisitor))
        .build();
  }

  public static OperationVisitor make(VariableVisitor variableVisitor) {
    var idVisitor = new IdVisitor(variableVisitor);
    var dateVisitor = new DateVisitor(variableVisitor);
    var argentVisitor = new ArgentVisitor(new ExpressionVisitor());

    return make(variableVisitor, idVisitor, argentVisitor, dateVisitor);
  }
}
