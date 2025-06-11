package school.hei.patrimoine.patrilang.modele;

public record Variable<T>(String name, VariableType type, T value) {}
