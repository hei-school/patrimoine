package school.hei.patrimoine.patrilang.unit.visitors.pj;

import static java.time.Month.JUNE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.pj.PiecesJustificative;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.pj.PieceJustfificativeVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class PieceJustfificativeVisitorTest {
  private static final LocalDate AJD = LocalDate.of(2025, JUNE, 23);
  private static final VariableVisitor variableVisitor = new VariableVisitor();

  static {
    variableVisitor.addToScope("ajd", DATE, AJD);
  }

  private final PieceJustfificativeVisitor subject =
      new PieceJustfificativeVisitor(variableVisitor, new IdVisitor(variableVisitor));

  private final UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public PiecesJustificative visitPieceJustificative(
            PatriLangParser.PieceJustificativeContext ctx) {
          return subject.apply(ctx);
        }
      };

  @Test
  void parse_piece_with_date_and_drive_link() {
    var input =
        """
        * `pj1` Dates:ajd, https://drive.example.com/doc1.pdf
        """;

    PiecesJustificative actual = visitor.visit(input, PatriLangParser::pieceJustificative);

    assertEquals("pj1", actual.idFluxArgent());
    assertEquals(AJD, actual.dateEmission());
    assertEquals("https://drive.example.com/doc1.pdf", actual.driveLink());
  }
}
