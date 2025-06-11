package school.hei.patrimoine.patrilang.modele;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VariableContainer {
  private final Map<VariableType, Set<Variable<?>>> values = new HashMap<>();

  public boolean exists(Variable<?> variable) {
    var container = this.values.getOrDefault(variable.type(), new HashSet<>());
    return container.stream().anyMatch(instance -> instance.name().equals(variable.name()));
  }

  public void add(Variable<?> variable) {
    if (exists(variable)) {
      throw new IllegalArgumentException(
          "La variable " + variable.name() + " a déjà été définie pour le type " + variable.type());
    }

    var container = this.values.getOrDefault(variable.type(), new HashSet<>());
    container.add(variable);
    this.values.put(variable.type(), container);
  }

  public Variable<?> get(String name, VariableType type) {
    var container = this.values.getOrDefault(type, new HashSet<>());
    return container.stream()
        .filter(variable -> variable.name().equals(name))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "La variable " + name + " de type " + type + " n'existe pas"));
  }
}
