package school.hei.patrimoine.patrilang;

import static java.util.Objects.isNull;
import static org.antlr.v4.runtime.CharStreams.fromFileName;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CasContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.DocumentContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ToutCasContext;

import java.io.IOException;
import org.antlr.v4.runtime.CommonTokenStream;
import school.hei.patrimoine.patrilang.antlr.PatriLangLexer;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.PiecesJustificativesContext;
import school.hei.patrimoine.patrilang.listener.PatrilangErrorListener;

public class PatriLangParser {
  public static ToutCasContext parseToutCas(String casSetPath) {
    var document = parse(casSetPath);

    if (isNull(document.toutCas())) {
      throw new IllegalArgumentException("Fichier CasSet attendu, mais fichier Cas trouvé.");
    }

    return document.toutCas();
  }

  public static CasContext parseCas(String casSetPath) {
    var document = parse(casSetPath);

    if (isNull(document.cas())) {
      throw new IllegalArgumentException("Fichier Cas attendu, mais fichier CasSet trouvé.");
    }

    return document.cas();
  }

  public static PiecesJustificativesContext parsePieceJustificative(String pjPath) {
    var document = parse(pjPath);

    if (isNull(document.piecesJustificatives())) {
      throw new IllegalArgumentException(
          "Fichier PieceJustificative attendu, mais un autre type de fichier trouvé.");
    }

    return document.piecesJustificatives();
  }

  public static DocumentContext parse(String filePath) {
    try {
      var lexer = new PatriLangLexer(fromFileName(filePath));
      var tokens = new CommonTokenStream(lexer);
      var parser = new school.hei.patrimoine.patrilang.antlr.PatriLangParser(tokens);
      var errorListener = new PatrilangErrorListener(filePath);

      parser.removeErrorListeners();
      parser.addErrorListener(errorListener);

      lexer.removeErrorListeners();
      lexer.addErrorListener(errorListener);
      return parser.document();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
