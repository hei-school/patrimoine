package school.hei.patrimoine.patrilang.modele.variable;

import java.util.*;

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

  public Optional<Variable<?>> find(String name, VariableType type) {
    var container = this.values.getOrDefault(type, new HashSet<>());
    return container.stream().filter(variable -> variable.name().equals(name)).findFirst();
  }
}
