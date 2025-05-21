package school.hei.patrimoine.patrilang;

import static java.util.Objects.isNull;
import static org.antlr.v4.runtime.CharStreams.fromFileName;

import java.io.IOException;
import java.nio.file.Paths;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.patrilang.antlr.PatriLangLexer;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.visitors.PatriLangVisitor;
import school.hei.patrimoine.patrilang.visitors.SectionVisitor;

public class PatriLangTranspiler {
  private static final String CAS_FILE_EXTENSION = ".cas.md";

  public static Cas transpileCas(String casName, SectionVisitor sectionVisitor) throws IOException {
    var casPath =
        Paths.get(sectionVisitor.casSetFolderPath())
            .resolve(casName + CAS_FILE_EXTENSION)
            .toAbsolutePath();
    var parser = createParser(fromFileName(casPath.toString()));
    var tree = parser.document();
    var visitor = PatriLangVisitor.create(sectionVisitor);

    if (isNull(tree.cas())) {
      throw new IllegalArgumentException("Expected a Cas file but found a CasSet file.");
    }

    return (Cas) visitor.visitDocument(tree);
  }

  public static CasSet transpileToutCas(String casSetPath) throws IOException {
    var parser = createParser(fromFileName(casSetPath));
    var tree = parser.document();
    var casSetFolderPath = Paths.get(casSetPath).getParent().toAbsolutePath();
    var visitor = PatriLangVisitor.create(SectionVisitor.create(casSetFolderPath.toString()));

    if (isNull(tree.toutCas())) {
      throw new IllegalArgumentException("Expected a CasSet file but found a Cas file.");
    }

    return (CasSet) visitor.visitDocument(tree);
  }

  private static PatriLangParser createParser(CharStream charStream) {
    var lexer = new PatriLangLexer(charStream);
    var tokens = new CommonTokenStream(lexer);
    return new PatriLangParser(tokens);
  }
}
