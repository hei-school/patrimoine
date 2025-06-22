package school.hei.patrimoine.patrilang.factory;

import java.util.Optional;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;
import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.possession.*;

public class SectionVisitorFactory {
  public static SectionVisitor make(String casSetFolderPath) {
    return make(casSetFolderPath, Optional.empty());
  }

  public static SectionVisitor make(String casSetFolderPath, Optional<VariableScope> parentScope) {
    var variableVisitor = new VariableVisitor(parentScope);
    var idVisitor = new IdVisitor(variableVisitor);
    var dateVisitor = new DateVisitor(variableVisitor);
    var argentVisitor = new ArgentVisitor(new ExpressionVisitor());

    var operationVisitor =
        OperationVisitorFactory.make(variableVisitor, idVisitor, argentVisitor, dateVisitor);

    return SectionVisitor.builder()
        .casSetFolderPath(casSetFolderPath)
        .dateVisitor(dateVisitor)
        .argentVisitor(argentVisitor)
        .operationVisitor(operationVisitor)
        .variableVisitor(variableVisitor)
        .compteVisitor(new CompteVisitor(dateVisitor, argentVisitor))
        .creanceVisitor(new CreanceVisitor(dateVisitor, argentVisitor))
        .detteVisitor(new DetteVisitor(dateVisitor, argentVisitor))
        .build();
  }
}
