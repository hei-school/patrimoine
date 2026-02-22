package school.hei.patrimoine.patrilang.files.validator;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpilePieceJustificative;

import java.util.function.Consumer;
import school.hei.patrimoine.patrilang.files.PatriLangFile;

public class PJFileValidator implements Consumer<PatriLangFile> {
  @Override
  public void accept(PatriLangFile pj) {
    transpilePieceJustificative(pj);
  }
}
