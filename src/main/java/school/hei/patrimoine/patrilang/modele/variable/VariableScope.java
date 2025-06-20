package school.hei.patrimoine.patrilang.modele.variable;

import java.util.Optional;

@SuppressWarnings("all")
public record VariableScope(VariableContainer container, Optional<VariableScope> parentScope) {

  public VariableScope(Optional<VariableScope> parentScope) {
    this(new VariableContainer(), parentScope);
  }

  private <T> Optional<Variable<T>> find(String name, VariableType type) {
    var variable = this.container.find(name, type);

    if (variable.isEmpty()) {
      if (this.parentScope.isEmpty()) {
        return Optional.empty();
      }

      return this.parentScope.get().find(name, type);
    }

    return Optional.ofNullable((Variable<T>) variable.get());
  }

  public <T> Variable<T> get(String name, VariableType type) {
    var variableValue = this.find(name, type);

    if (variableValue.isEmpty()) {
      throw new IllegalArgumentException(
          "La variable " + name + " de type " + type + " n'existe pas");
    }

    return (Variable<T>) variableValue.get();
  }

  public <T> void add(String name, VariableType type, T value) {
    this.container.add(new Variable(name, type, value));
  }
}
