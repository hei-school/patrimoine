package school.hei.patrimoine.patrilang;

import java.util.Map;
import java.util.function.Function;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import school.hei.patrimoine.patrilang.antlr.PatriLangLexer;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;

public class PatriLangTranspiler implements Function<CharStream, Map<String, String>> {

  @Override
  public Map<String, String> apply(CharStream charStream) {
    var lexer = new PatriLangLexer(charStream);
    var tokens = new CommonTokenStream(lexer);
    var parser = new PatriLangParser(tokens);
    var visitor = new PatriLangTranspileVisitor();
    var tree = parser.document();
    return visitor.visit(tree);
  }
}
