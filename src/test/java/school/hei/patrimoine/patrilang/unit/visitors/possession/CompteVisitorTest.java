package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.ArgentVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.CompteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class CompteVisitorTest {
  CompteVisitor subject =
      new CompteVisitor(new VariableVisitor(), new ArgentVisitor(new VariableVisitor()));

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public Compte visitCompte(CompteContext ctx) {
          return subject.apply(ctx);
        }
      };

  @Test
  void parse_normal_compte() {
    var input = "monCompte, valant 300000Ar le 01 du 01-2025";

    var expected = new Compte("monCompte", LocalDate.of(2025, JANUARY, 1), ariary(300_000));

    Compte actual = visitor.visit(input, PatriLangParser::compte);

    assertEquals(expected.nom(), actual.nom());
    assertEquals(expected.getDateOuverture(), actual.getDateOuverture());
    assertEquals(expected.valeurComptable(), actual.valeurComptable());
  }
}
