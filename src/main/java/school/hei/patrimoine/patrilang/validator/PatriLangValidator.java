package school.hei.patrimoine.patrilang.validator;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;

import java.util.function.Consumer;
import school.hei.patrimoine.patrilang.modele.PatriLangCas;

public class PatriLangValidator implements Consumer<String> {
  @Override
  public void accept(String casSetPath) {
    var casSet = transpileToutCas(casSetPath);

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
