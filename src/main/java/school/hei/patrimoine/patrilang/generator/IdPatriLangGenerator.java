package school.hei.patrimoine.patrilang.generator;

import static school.hei.patrimoine.modele.normalizer.PossessionNomNormalizer.normalize;

public class IdPatriLangGenerator implements PatriLangGenerator<String> {
  @Override
  public String apply(String s) {
    return normalize(s);
  }
}
