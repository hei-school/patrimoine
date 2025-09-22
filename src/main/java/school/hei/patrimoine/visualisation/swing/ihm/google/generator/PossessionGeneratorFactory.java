package school.hei.patrimoine.visualisation.swing.ihm.google.generator;

import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession.ExecutionGenerator;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession.FluxArgentExecutionGenerator;

public class PossessionGeneratorFactory {
  public static String MULTIPLE_EXECUTION_NOM_FORMAT = "[%s]__%s";

  @SuppressWarnings("unchecked")
  public static <T> ExecutionGenerator<T> make(T possession) {
    if (possession instanceof FluxArgent) {
      return (ExecutionGenerator<T>) new FluxArgentExecutionGenerator();
    }

    throw new IllegalArgumentException(
        String.format(
            "Generating instance of clazz=%s is not supported yet", possession.getClass()));
  }
}
