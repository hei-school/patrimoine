package school.hei.patrimoine.patrilang.generator;

import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.patrilang.generator.possession.FluxArgentPatriLangGenerator;
import school.hei.patrimoine.patrilang.generator.possession.TransfertArgentPatriLangGenerator;

@SuppressWarnings("unchecked")
public class PatriLangGeneratorFactory {
  public static <T> PatriLangGenerator<T> make(T data) {
    if (data instanceof FluxArgent) {
      return (PatriLangGenerator<T>) new FluxArgentPatriLangGenerator();
    }

    if (data instanceof TransfertArgent) {
      return (PatriLangGenerator<T>) new TransfertArgentPatriLangGenerator();
    }

    throw new IllegalArgumentException("Not Supported Yet");
  }
}
