package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.*;
import school.hei.patrimoine.patrilang.antlr.PatriLangLexer;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;

public class PatriLangSyntaxValidator {
  public static List<String> validate(String content) {
    var errors = new ArrayList<String>();

    var lexer = new PatriLangLexer(CharStreams.fromString(content));
    var tokens = new CommonTokenStream(lexer);
    var parser = new PatriLangParser(tokens);

    parser.removeErrorListeners();
    lexer.removeErrorListeners();

    BaseErrorListener listener =
        new BaseErrorListener() {
          @Override
          public void syntaxError(
              Recognizer<?, ?> recognizer,
              Object offendingSymbol,
              int line,
              int charPositionInLine,
              String msg,
              RecognitionException e) {
            errors.add("Ligne " + line + ":" + charPositionInLine + " → " + msg);
          }
        };

    lexer.addErrorListener(listener);
    parser.addErrorListener(listener);

    parser.document();

    return errors;
  }
}
