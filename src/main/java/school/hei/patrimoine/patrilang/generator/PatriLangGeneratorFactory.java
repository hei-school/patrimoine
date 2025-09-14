package school.hei.patrimoine.patrilang.generator;

import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.generator.possession.FluxArgentPatriLangGenerator;

@SuppressWarnings("unchecked")
public class PatriLangGeneratorFactory {
  public static <T> PatriLangGenerator<T> make(T data) {
    if (data instanceof FluxArgent) {
      return (PatriLangGenerator<T>) new FluxArgentPatriLangGenerator();
    }

    throw new IllegalArgumentException("Not Supported Yet");
  }
}
