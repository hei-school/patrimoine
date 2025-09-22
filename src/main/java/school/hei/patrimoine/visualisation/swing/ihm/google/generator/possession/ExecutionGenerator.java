package school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession;

import java.util.Map;
import java.util.function.Function;

public interface ExecutionGenerator<T> extends Function<Map<String, Object>, T> {
  void validateArgs(Map<String, Object> args) throws IllegalArgumentException;
}
