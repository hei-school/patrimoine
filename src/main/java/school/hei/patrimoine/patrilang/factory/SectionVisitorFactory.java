package school.hei.patrimoine.patrilang.factory;

import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.possession.*;

public class SectionVisitorFactory {
  public static SectionVisitor create(String casSetFolderPath) {
    var dateVisitor = new VariableVisitor<>(DateContext.class, BaseVisitor::visitDate);
    var personVisitor =
        new VariableVisitor<>(TextContext.class, BaseVisitor::visitPersonne, Personne::nom);

    var compteVisitor = new CompteVisitor(dateVisitor);
    var creanceVisitor = new CreanceVisitor(dateVisitor);
    var detteVisitor = new DetteVisitor(dateVisitor);

    var variableCompteVisitor =
        new VariableVisitor<>(CompteContext.class, compteVisitor, Compte::nom);
    var variableCreanceVisitor =
        new VariableVisitor<>(CompteContext.class, creanceVisitor, Creance::nom);
    var variableDetteVisitor = new VariableVisitor<>(CompteContext.class, detteVisitor, Dette::nom);

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
