package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.ValeurMarche.ValeurMarche;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.ValeurMarcheVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class ValeurMarcheVisitorTest {
  private static final LocalDate AJD = LocalDate.of(2025, 7, 27);
  private static final VariableVisitor variableVisitor = new VariableVisitor();
  private static final Compte MON_COMPTE = new Compte("monCompte", AJD, ariary(500_000));

  ValeurMarcheVisitor subject = new ValeurMarcheVisitor(variableVisitor);

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public ValeurMarche visitValeurMarche(PatriLangParser.ValeurMarcheContext ctx) {
          return subject.apply(ctx);
        }
      };

  static {
    variableVisitor.addToScope("ajd", DATE, AJD);
    variableVisitor.addToScope("monCompte", TRESORERIES, MON_COMPTE);
  }

  @Test
  void parse_valeur_marche_compte_should_throw_unsupported_operation() {
    var input =
        """
            * `valeurMarche1`, Dates:ajd valeur marché de 600000Ar pour Trésoreries:monCompte
        """;

    var exception =
        assertThrows(
            UnsupportedOperationException.class,
            () -> visitor.visit(input, PatriLangParser::valeurMarche));

    String expectedMessage =
        "Seules les possessions de type IMMOBILISATION ou ENTREPRISE peuvent avoir une valeur de"
            + " marché.";
    assertEquals(expectedMessage, exception.getMessage());
  }
}
