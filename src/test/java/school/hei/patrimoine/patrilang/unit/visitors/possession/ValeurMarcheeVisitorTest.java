package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.ValeurMarcheeVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class ValeurMarcheeVisitorTest {
  private static final VariableVisitor variableVisitor = new VariableVisitor();
  private static final ValeurMarcheeVisitor subject = new ValeurMarcheeVisitor(variableVisitor);

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public Object visitAjoutValeurMarchee(PatriLangParser.AjoutValeurMarcheeContext ctx) {
          return subject.apply(ctx);
        }
      };

  @Test
  void throws_exception_when_possession_type_is_not_immobilisation_or_entreprise() {
    variableVisitor.addToScope(
        "maPossession",
        TRESORERIES,
        new Compte("maPossession", LocalDate.of(2025, 1, 1), ariary(200_000)));

    var input =
        "*`valeurMarchée`, le 01 du 01-2025, valeur marchée de 200000Ar pour"
            + " Trésoreries:maPossession";

    var exception =
        assertThrows(
            UnsupportedOperationException.class,
            () -> visitor.visit(input, PatriLangParser::ajoutValeurMarchee));

    assertTrue(exception.getMessage().contains("Impossible d'ajouter une valeur de marché"));
  }
}
