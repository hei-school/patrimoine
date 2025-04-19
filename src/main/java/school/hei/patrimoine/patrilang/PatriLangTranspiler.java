package school.hei.patrimoine.patrilang;

import java.util.function.Function;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.patrilang.antlr.PatriLangLexer;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.visitors.*;

public class PatriLangTranspiler implements Function<CharStream, Patrimoine> {
  private static final CompteVisitor compteVisitor = new CompteVisitor();
  private static final CreanceVisitor creanceVisitor = new CreanceVisitor();
  private static final DetteVisitor detteVisitor = new DetteVisitor();
  private static final MaterielVisitor materielVisitor = new MaterielVisitor();
  private static final AchatMaterielVisitor achatMaterielVisitor = new AchatMaterielVisitor();
  private static final FluxArgentVisitor fluxArgentVisitor = new FluxArgentVisitor();
  private static final TransferArgentVisitor transferArgentVisitor = new TransferArgentVisitor();
  private static final GroupPossessionVisitor groupPossessionVisitor = new GroupPossessionVisitor();

  @Override
  public Patrimoine apply(CharStream charStream) {
    var lexer = new PatriLangLexer(charStream);
    var tokens = new CommonTokenStream(lexer);
    var parser = new PatriLangParser(tokens);
    var visitor =
        new PatriLangTranspileVisitor(
            compteVisitor,
            creanceVisitor,
            detteVisitor,
            materielVisitor,
            achatMaterielVisitor,
            fluxArgentVisitor,
            transferArgentVisitor,
            groupPossessionVisitor);

    var tree = parser.document();

    return (Patrimoine) visitor.visit(tree);
  }
}
