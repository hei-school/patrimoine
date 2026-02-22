package school.hei.patrimoine.patrilang.files.validator;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;

import java.util.function.Consumer;
import school.hei.patrimoine.patrilang.files.PatriLangFile;
import school.hei.patrimoine.patrilang.modele.PatriLangCas;

public class CasSetFileValidator implements Consumer<PatriLangFile> {
  @Override
  public void accept(PatriLangFile casSetFile) {
    var casSet = transpileToutCas(casSetFile);

    // Must visit each Cas individually because evaluation is lazy.
    // Visiting ensures that all sections are processed and validated.
    casSet
        .set()
        .forEach(
            cas -> {
              if (cas instanceof PatriLangCas patriLangCas) {
                patriLangCas.validate();
              }
            });
  }
}
