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
    var expressionVisitor = new ExpressionVisitor();
    var idVisitor = new IdVisitor(variableVisitor);
    var dateVisitor = new DateVisitor(variableVisitor);
    var argentVisitor = new ArgentVisitor(expressionVisitor);

    return SectionVisitor.builder()
        .casSetFolderPath(casSetFolderPath)
        .variableVisitor(variableVisitor)
        .dateVisitor(dateVisitor)
        .argentVisitor(argentVisitor)
        .expressionVisitor(expressionVisitor)
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
