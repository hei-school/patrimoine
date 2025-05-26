package school.hei.patrimoine.patrilang.factory;

import school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.possession.*;

public class SectionVisitorFactory {
  public static SectionVisitor create(String casSetFolderPath) {
    var dateVisitor = new VariableVisitor<>(DateContext.class, BaseVisitor::visitDate);
    var personVisitor = new VariableVisitor<>(TextContext.class, BaseVisitor::visitPersonne);

    var compteVisitor = new CompteVisitor(dateVisitor);
    var creanceVisitor = new CreanceVisitor(dateVisitor);
    var detteVisitor = new DetteVisitor(dateVisitor);

    var variableCompteVisitor = new VariableVisitor<>(CompteContext.class, compteVisitor);
    var variableCreanceVisitor = new VariableVisitor<>(CompteContext.class, creanceVisitor);
    var variableDetteVisitor = new VariableVisitor<>(CompteContext.class, detteVisitor);

    return new SectionVisitor(
        casSetFolderPath,
        new ObjectifVisitor(dateVisitor, variableCompteVisitor),
        new MaterielVisitor(dateVisitor),
        new AchatMaterielVisitor(dateVisitor, variableCompteVisitor),
        new FluxArgentVisitor(dateVisitor, variableCompteVisitor),
        new TransferArgentVisitor(dateVisitor, variableCompteVisitor),
        new CorrectionVisitor(dateVisitor, variableCompteVisitor),
        new GroupPossessionVisitor(dateVisitor),
        dateVisitor,
        personVisitor,
        variableCompteVisitor,
        variableDetteVisitor,
        variableCreanceVisitor);
  }
}
