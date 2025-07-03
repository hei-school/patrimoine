package school.hei.patrimoine.patrilang.visitors;

import java.util.function.Function;

public interface SimpleVisitor<C, T> extends Function<C, T> {}
