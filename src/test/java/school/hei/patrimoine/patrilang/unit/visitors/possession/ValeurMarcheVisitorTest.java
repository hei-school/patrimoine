package school.hei.patrimoine.patrilang.unit.visitors.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.vente.ValeurMarche;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.ValeurMarcheContext;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.ValeurMarcheVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

public class ValeurMarcheVisitorTest {
  private static final VariableVisitor variableVisitor = new VariableVisitor();
  private static final LocalDate AJD = LocalDate.now();
  ValeurMarcheVisitor subject = new ValeurMarcheVisitor(new VariableVisitor());

  UnitTestVisitor visitor =
          new UnitTestVisitor() {
            @Override
            public ValeurMarche visitValeurMarche(ValeurMarcheContext ctx) {
              return subject.apply(ctx);
            }
          };

  static {
    variableVisitor.addToScope("ajd", DATE, AJD);
  }

  @Test
  void parse_valeur_marche() {
    var input = "* `valeurMarche` Dates:ajd, possessionTest, valant 1000000Ar";

    Possession possession = new Materiel("maisonTest", LocalDate.now(),LocalDate.now(), new Argent(1_000_000, Devise.MGA), 2.0);
    var expected = new ValeurMarche(possession, LocalDate.now(), ariary(1_000_000));

    ValeurMarche actual = visitor.visit(input, PatriLangParser::valeurMarche);

    assertEquals(expected.t(), actual.t());
    assertEquals(expected.valeur(), actual.valeur());
//    assertEquals(expercted.possession(), actual.possession());
  }
}
