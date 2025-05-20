package school.hei.patrimoine.patrilang.modele;

import java.util.HashSet;
import java.util.Set;
import school.hei.patrimoine.Pair;

public class VariableContainer<T> {
  private final Set<Pair<String, T>> values = new HashSet<>();

  public boolean exists(String name) {
    return this.values.stream().anyMatch(value -> value.first().equals(name));
  }

  public void add(Pair<String, T> value) {
    if (exists(value.first())) {
      throw new IllegalArgumentException("Variable " + value.first() + " already exists");
    }

    this.values.add(value);
  }

  public void addAll(Set<Pair<String, T>> values) {
    values.forEach(this::add);
  }

  public T get(String name) {
    var optionalValue =
        this.values.stream().filter(value -> value.first().equals(name)).findFirst();
    return optionalValue
        .orElseThrow(() -> new IllegalArgumentException("Variable " + name + " doesn't exist"))
        .second();
  }
}
