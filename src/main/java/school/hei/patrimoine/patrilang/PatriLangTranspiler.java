package school.hei.patrimoine.patrilang;

import java.util.function.Function;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.patrilang.antlr.PatriLangLexer;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.visitors.SectionVisitor;

public class PatriLangTranspiler implements Function<CharStream, Patrimoine> {
  @Override
  public Patrimoine apply(CharStream charStream) {
    var lexer = new PatriLangLexer(charStream);
    var tokens = new CommonTokenStream(lexer);
    var parser = new PatriLangParser(tokens);

    var sectionVisitor = SectionVisitor.create();
    var toutCasVisitor = new PatriLangToutCasVisitor(sectionVisitor);
    var casVisitor = new PatriLangCasVisitor(sectionVisitor);
    var visitor = new PatriLangTranspileVisitor(toutCasVisitor, casVisitor);

    var tree = parser.document();

    return (Patrimoine) visitor.visit(tree);
  }
}
