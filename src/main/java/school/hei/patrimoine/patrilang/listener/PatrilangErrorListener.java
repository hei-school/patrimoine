package school.hei.patrimoine.patrilang.listener;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import school.hei.patrimoine.patrilang.files.PatriLangFile;

@RequiredArgsConstructor
public class PatrilangErrorListener extends BaseErrorListener {
  private final PatriLangFile file;

  @Override
  public void syntaxError(
      Recognizer<?, ?> recognizer,
      Object offendingSymbol,
      int line,
      int charPositionInLine,
      String msg,
      RecognitionException e) {

    var errorMessage =
        String.format(
            "Erreur de syntaxe à la ligne %d, colonne %d dans le fichier '%s', Raison: %s",
            line, charPositionInLine, file.getAbsolutePath(), msg);

    throw new ParseCancellationException(errorMessage, e);
  }
}
