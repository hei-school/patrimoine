package school.hei.patrimoine.patrilang.visitors.possession;

import java.util.function.Function;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.DeclarationValeurMarcheContext;

@FunctionalInterface
public interface ValeurMarcheVisitor extends Function<DeclarationValeurMarcheContext, Void> {
  @Override
  default Void apply(DeclarationValeurMarcheContext ctx) {
    visit(ctx);
    return null;
  }

  void visit(DeclarationValeurMarcheContext ctx);
}
