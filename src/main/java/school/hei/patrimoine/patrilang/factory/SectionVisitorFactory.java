package school.hei.patrimoine.patrilang.factory;

import java.util.Optional;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;
import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.possession.*;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class SectionVisitorFactory {
  public static SectionVisitor make(String casSetFolderPath) {
    return make(casSetFolderPath, Optional.empty());
  }

  public static SectionVisitor make(String casSetFolderPath, Optional<VariableScope> parentScope) {
    var variableVisitor = new VariableVisitor(parentScope);
    var idVisitor = new IdVisitor(variableVisitor);

    var operationVisitor = OperationVisitorFactory.make(variableVisitor, idVisitor);

    return SectionVisitor.builder()
        .casSetFolderPath(casSetFolderPath)
        .operationVisitor(operationVisitor)
        .variableVisitor(variableVisitor)
        .operationTemplateVisitor(new OperationTemplateVisitor())
        .compteVisitor(new CompteVisitor(variableVisitor))
        .creanceVisitor(new CreanceVisitor(variableVisitor))
        .detteVisitor(new DetteVisitor(variableVisitor))
        .build();
  }
}
