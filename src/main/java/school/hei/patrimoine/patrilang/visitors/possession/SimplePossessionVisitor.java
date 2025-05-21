package school.hei.patrimoine.patrilang.visitors.possession;

import java.util.function.Function;
import org.antlr.v4.runtime.ParserRuleContext;
import school.hei.patrimoine.modele.possession.Possession;

public interface SimplePossessionVisitor<
        ContextType extends ParserRuleContext, PossessionType extends Possession>
    extends Function<ContextType, PossessionType> {}
