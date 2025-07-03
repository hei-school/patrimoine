package school.hei.patrimoine.patrilang;

import static java.util.Objects.isNull;
import static org.antlr.v4.runtime.CharStreams.fromFileName;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DocumentContext;

import java.io.IOException;
import java.nio.file.Paths;
import org.antlr.v4.runtime.CommonTokenStream;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.patrilang.antlr.PatriLangLexer;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.factory.SectionVisitorFactory;
import school.hei.patrimoine.patrilang.visitors.PatriLangCasVisitor;
import school.hei.patrimoine.patrilang.visitors.PatriLangToutCasVisitor;
import school.hei.patrimoine.patrilang.visitors.PatriLangVisitor;
import school.hei.patrimoine.patrilang.visitors.SectionVisitor;

public class PatriLangTranspiler {
  public static final String CAS_FILE_EXTENSION = ".cas.md";
  public static final String TOUT_CAS_FILE_EXTENSION = ".tout.md";

  public static Cas transpileCas(String casName, SectionVisitor sectionVisitor) {
    var casPath =
        Paths.get(sectionVisitor.getCasSetFolderPath())
            .resolve(casName + CAS_FILE_EXTENSION)
            .toAbsolutePath();
    var tree = parseAsTree(casPath.toString());

    if (isNull(tree.cas())) {
      throw new IllegalArgumentException("Fichier Cas attendu, mais fichier CasSet trouvé.");
    }

    var patrilangVisitor = new PatriLangVisitor(null, new PatriLangCasVisitor(sectionVisitor));
    return (Cas) patrilangVisitor.visitDocument(tree);
  }

  public static CasSet transpileToutCas(String casSetPath) {
    var tree = parseAsTree(casSetPath);

    if (isNull(tree.toutCas())) {
      throw new IllegalArgumentException("Fichier CasSet attendu, mais fichier Cas trouvé.");
    }

    var casSetFolderPath = Paths.get(casSetPath).getParent().toAbsolutePath();
    var sectionVisitor = SectionVisitorFactory.make(casSetFolderPath.toString());
    var patrilangVisitor = new PatriLangVisitor(new PatriLangToutCasVisitor(sectionVisitor), null);

    return (CasSet) patrilangVisitor.visitDocument(tree);
  }

  private static DocumentContext parseAsTree(String filePath) {
    try {
      var lexer = new PatriLangLexer(fromFileName(filePath));
      var tokens = new CommonTokenStream(lexer);
      var parser = new PatriLangParser(tokens);
      return parser.document();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
