package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.vente.ValeurMarche;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.ValeurMarcheVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class ValeurMarcheVisitorTest {
  private static final LocalDate AJD = LocalDate.now();
  private static final LocalDate DATE_PASSEE = LocalDate.now().minusYears(1);
  private static final LocalDate DATE_FUTURE = LocalDate.now().plusYears(1);

  private VariableVisitor variableVisitor;
  private ValeurMarcheVisitor subject;
  private UnitTestVisitor visitor;

  @BeforeEach
  void setUp() {
    variableVisitor = new VariableVisitor();
    subject = new ValeurMarcheVisitor(variableVisitor);
    visitor =
        new UnitTestVisitor() {
          @Override
          public ValeurMarche visitValeurMarche(PatriLangParser.ValeurMarcheContext ctx) {
            return subject.apply(ctx);
          }
        };
  }

  @Test
  void parse_valeur_marche_materiel() {
    Materiel possession =
        new Materiel("maisonTest", AJD, AJD, new Argent(1_000_000, Devise.MGA), 2.0);
    variableVisitor.addToScope("possessionTest", TRESORERIES, possession);
    variableVisitor.addToScope("ajd", DATE, AJD);

    var input = "* `valeurMarche` Dates:ajd, Trésoreries:possessionTest valant 1000000Ar";

    ValeurMarche expected = new ValeurMarche(possession, AJD, ariary(1_000_000));
    ValeurMarche actual = visitor.visit(input, PatriLangParser::valeurMarche);

    assertEquals(expected.t(), actual.t());
    assertEquals(expected.valeur().getMontant(), actual.valeur().getMontant());
    assertEquals(expected.possession().nom(), actual.possession().nom());
  }

  @Test
  void parse_valeur_marche_date_passee() {
    Materiel possession =
        new Materiel("voiture", DATE_PASSEE, DATE_PASSEE, ariary(10_000_000), 10.0);
    variableVisitor.addToScope("voitureTest", TRESORERIES, possession);
    variableVisitor.addToScope("datePassee", DATE, DATE_PASSEE);

    var input = "* `valeurMarche` Dates:datePassee, Trésoreries:voitureTest valant 8000000Ar";

    ValeurMarche expected = new ValeurMarche(possession, DATE_PASSEE, ariary(8_000_000));
    ValeurMarche actual = visitor.visit(input, PatriLangParser::valeurMarche);

    assertEquals(expected.t(), actual.t());
    assertEquals(expected.valeur().getMontant(), actual.valeur().getMontant());
  }

  @Test
  void parse_valeur_marche_date_future() {
    Materiel possession = new Materiel("ordinateur", AJD, AJD, ariary(2_000_000), 5.0);
    variableVisitor.addToScope("ordiTest", TRESORERIES, possession);
    variableVisitor.addToScope("dateFuture", DATE, DATE_FUTURE);

    var input = "* `valeurMarche` Dates:dateFuture, Trésoreries:ordiTest valant 1500000Ar";

    ValeurMarche expected = new ValeurMarche(possession, DATE_FUTURE, ariary(1_500_000));
    ValeurMarche actual = visitor.visit(input, PatriLangParser::valeurMarche);

    assertEquals(expected.t(), actual.t());
    assertEquals(expected.valeur().getMontant(), actual.valeur().getMontant());
  }

  @Test
  void parse_valeur_marche_possession_inconnue() {
    variableVisitor.addToScope("ajd", DATE, AJD);

    var input = "* `valeurMarche` Dates:ajd, Trésoreries:inexistante valant 1000Ar";

    assertThrows(
        IllegalArgumentException.class, () -> visitor.visit(input, PatriLangParser::valeurMarche));
  }
}
