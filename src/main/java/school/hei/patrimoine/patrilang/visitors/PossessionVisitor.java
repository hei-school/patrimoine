package school.hei.patrimoine.patrilang.visitors;

import org.antlr.v4.runtime.ParserRuleContext;
import school.hei.patrimoine.modele.possession.Possession;

public interface PossessionVisitor<PossessionType extends Possession, ContextType extends ParserRuleContext> {
    PossessionType visit(ContextType ctx);
}