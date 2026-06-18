package school.hei.patrimoine.patrilang;

import static school.hei.patrimoine.patrilang.PatriLangParser.parseCas;
import static school.hei.patrimoine.patrilang.PatriLangParser.parsePieceJustificative;
import static school.hei.patrimoine.patrilang.PatriLangParser.parseToutCas;

import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.possession.pj.OperationComment;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.patrilang.files.PatriLangFile;
import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.factory.SectionVisitorFactory;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class PatriLangTranspiler implements Function<String, CasSet> {
  // TODO: extension to change for pj
  public static final String INFO_FILE_EXTENSION = ".info.md";
  public static final String CAS_FILE_EXTENSION = ".cas.md";
  public static final String TOUT_CAS_FILE_EXTENSION = ".tout.md";

  @Override
  public CasSet apply(String casSetFilePath) {
    return transpileToutCas(casSetFilePath);
  }

  public static Cas transpileCas(String casName, SectionVisitor sectionVisitor) {
    var casPath =
        Paths.get(sectionVisitor.getCasSetFolderPath())
            .resolve(casName + CAS_FILE_EXTENSION)
            .toAbsolutePath()
            .toString();
    var tree = parseCas(new PatriLangFile(casPath));
    var patrilangVisitor =
        new PatriLangVisitor(null, new PatriLangCasVisitor(sectionVisitor), null);
    return patrilangVisitor.visitCas(tree);
  }

  public static CasSet transpileToutCas(PatriLangFile casSetFile) {
    var tree = parseToutCas(casSetFile);
    var casSetFolderPath = casSetFile.toPath().getParent().toAbsolutePath();
    var sectionVisitor = SectionVisitorFactory.make(casSetFolderPath.toString());
    var patrilangVisitor =
        new PatriLangVisitor(new PatriLangToutCasVisitor(sectionVisitor), null, null);

    return patrilangVisitor.visitToutCas(tree);
  }

  private static OperationSupportingInformation transpileSupportingInfo(PatriLangFile file) {
    var tree = parsePieceJustificative(file);
    var variableVisitor = new VariableVisitor();
    var idVisitor = new IdVisitor(variableVisitor);
    var dateVisitor = variableVisitor.getVariableDateVisitor();
    var patrilangVisitor =
        PatriLangVisitor.builder()
            .pjAndCommentsVisitor(
                new PatriLangOperationSupportingInformationVisitor(
                    new PatriLangPieceJustificativeVisitor(idVisitor, dateVisitor),
                    new PatriLangCommentOperationVisitor(idVisitor, dateVisitor)))
            .build();
    return patrilangVisitor.visitSupportingInfos(tree);
  }

  public static List<PieceJustificative> transpilePieceJustificative(PatriLangFile file) {
    return transpileSupportingInfo(file).piecesJustificatives();
  }

  public static List<OperationComment> transpileComments(PatriLangFile file) {
    return transpileSupportingInfo(file).operationComments();
  }

  @Deprecated
  public static CasSet transpileToutCas(String casSetFile) {
    return transpileToutCas(new PatriLangFile(casSetFile));
  }

  @Deprecated
  public static List<PieceJustificative> transpilePieceJustificative(String pjFile) {
    return transpilePieceJustificative(new PatriLangFile(pjFile));
  }

  @Deprecated
  public static List<OperationComment> transpileComments(String pjFile) {
    return transpileComments(new PatriLangFile(pjFile));
  }
}
