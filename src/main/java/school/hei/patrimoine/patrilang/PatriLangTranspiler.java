package school.hei.patrimoine.patrilang;

import static school.hei.patrimoine.patrilang.PatriLangParser.parseCas;
import static school.hei.patrimoine.patrilang.PatriLangParser.parsePieceJustificative;

import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;
import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.factory.SectionVisitorFactory;
import school.hei.patrimoine.patrilang.visitors.variable.VariableDateVisitor;

public class PatriLangTranspiler implements Function<String, CasSet> {
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
            .toAbsolutePath();
    var tree = parseCas(casPath.toString());
    var patrilangVisitor =
        new PatriLangVisitor(null, new PatriLangCasVisitor(sectionVisitor), null);
    return patrilangVisitor.visitCas(tree);
  }

  public static CasSet transpileToutCas(String casSetPath) {
    var tree = PatriLangParser.parseToutCas(casSetPath);
    var casSetFolderPath = Paths.get(casSetPath).getParent().toAbsolutePath();
    var sectionVisitor = SectionVisitorFactory.make(casSetFolderPath.toString());
    var patrilangVisitor =
        new PatriLangVisitor(new PatriLangToutCasVisitor(sectionVisitor), null, null);

    return patrilangVisitor.visitToutCas(tree);
  }

  public static List<PieceJustificative> transpilePiecesJustificative(
      String pieceJustificativePath) {
    var tree = parsePieceJustificative(pieceJustificativePath);
    var patrilangVisitor =
        new PatriLangVisitor(
            null,
            null,
            new PatriLangPiecesJustificativeVisitor(
                new IdVisitor(null), new VariableDateVisitor(new VariableScope(), null)));
    return patrilangVisitor.visitPiecesJustificatives(tree);
  }
}
