package school.hei.patrimoine.patrilang.modele.variable;

public record Variable<T>(String name, VariableType type, T value) {}
