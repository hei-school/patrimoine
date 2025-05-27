package school.hei.patrimoine.patrilang.visitors.possession;

import java.util.function.Function;
import org.antlr.v4.runtime.ParserRuleContext;

public interface SimpleVisitor<C extends ParserRuleContext, T> extends Function<C, T> {}
