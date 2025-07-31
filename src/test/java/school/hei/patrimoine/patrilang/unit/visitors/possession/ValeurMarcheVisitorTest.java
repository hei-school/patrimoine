package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.vente.ValeurMarche;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.ValeurMarcheContext;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.ValeurMarcheVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class ValeurMarcheVisitorTest {
  private static final LocalDate AJD = LocalDate.now();
  private static final LocalDate DATE_PASSEE = AJD.minusYears(1);
  private static final LocalDate DATE_FUTURE = AJD.plusYears(1);
  private static final Materiel POSSESSION1 =
      new Materiel("Téléphone", AJD, AJD, new Argent(1_000_000, Devise.MGA), 2.0);
  private static final Materiel POSSESSION2 =
      new Materiel("voiture", DATE_PASSEE, DATE_PASSEE, ariary(10_000_000), 10.0);
  private static final Materiel POSSESSION3 =
      new Materiel("ordinateur", AJD, AJD, ariary(2_000_000), 5.0);

  private static final VariableVisitor variableVisitor = new VariableVisitor();

  ValeurMarcheVisitor subject = new ValeurMarcheVisitor(variableVisitor);

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public ValeurMarche visitValeurMarche(ValeurMarcheContext ctx) {
          return subject.apply(ctx);
        }
      };

  static {
    variableVisitor.addToScope("objet", MATERIEL, POSSESSION1);
    variableVisitor.addToScope("objetAvecDatePasse", MATERIEL, POSSESSION2);
    variableVisitor.addToScope("objetAvecDateFuture", MATERIEL, POSSESSION3);
    variableVisitor.addToScope("ajd", DATE, AJD);
    variableVisitor.addToScope("datePassee", DATE, DATE_PASSEE);
    variableVisitor.addToScope("dateFuture", DATE, DATE_FUTURE);
  }

  @Test
  void parse_valeur_marche_materiel() {

    var input =
        """
    * `valeurMarche` Dates:ajd, Matériel:objet valant 2000000Ar
""";

    var expected = new ValeurMarche(POSSESSION1, AJD, ariary(2_000_000));

    ValeurMarche actual = visitor.visit(input, PatriLangParser::valeurMarche);

    assertEquals(expected.t(), actual.t());
    assertEquals(expected.valeur(), actual.valeur());
    assertEquals(expected.possession().nom(), actual.possession().nom());
  }

  @Test
  void parse_valeur_marche_date_passee() {
    var input =
        """
    * `valeurMarche` Dates:datePassee, Matériel:objetAvecDatePasse valant 8000000Ar
""";

    var expected = new ValeurMarche(POSSESSION2, DATE_PASSEE, ariary(8_000_000));

    ValeurMarche actual = visitor.visit(input, PatriLangParser::valeurMarche);

    assertEquals(expected.t(), actual.t());
    assertEquals(expected.valeur(), actual.valeur());
  }

  @Test
  void parse_valeur_marche_date_future() {
    var input =
        """
        * `valeurMarche` Dates:dateFuture, Matériel:objetAvecDateFuture valant 1500000Ar
        """;

    var expected = new ValeurMarche(POSSESSION3, DATE_FUTURE, ariary(1_500_000));

    ValeurMarche actual = visitor.visit(input, PatriLangParser::valeurMarche);

    assertEquals(expected.t(), actual.t());
    assertEquals(expected.valeur(), actual.valeur());
  }

  @Test
  void parse_valeur_marche_possession_inconnue() {
    var input =
        """
        * `valeurMarche` Dates:ajd, Matériel:inexistante valant 1000Ar
        """;

    assertThrows(
        IllegalArgumentException.class, () -> visitor.visit(input, PatriLangParser::valeurMarche));
  }
}
