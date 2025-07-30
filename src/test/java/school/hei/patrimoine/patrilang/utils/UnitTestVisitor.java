package school.hei.patrimoine.patrilang.utils;

import java.util.function.Function;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.ParserRuleContext;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;

@SuppressWarnings("all")
public class UnitTestVisitor extends PatriLangParserBaseVisitor<Object> {
  public <T> T visit(String input, Function<PatriLangParser, ParserRuleContext> getContext) {
    var parser = createParser(input);
    return (T) this.visit(getContext.apply(parser));
  }

  public static PatriLangParser createParser(String input) {
    var charStream = CharStreams.fromString(input);
    var lexer = new PatriLangLexer(charStream);
    var parser = new PatriLangParser(new CommonTokenStream(lexer));

    parser.removeErrorListeners();
    parser.addErrorListener(new DiagnosticErrorListener());

    return parser;
  }
}
