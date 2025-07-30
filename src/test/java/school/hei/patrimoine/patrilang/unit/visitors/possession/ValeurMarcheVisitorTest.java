package school.hei.patrimoine.patrilang.unit.visitors.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.vente.ValeurMarche;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.ValeurMarcheVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;

public class ValeurMarcheVisitorTest {
  private static final LocalDate AJD = LocalDate.now();
  private static final VariableVisitor variableVisitor = new VariableVisitor();

  ValeurMarcheVisitor subject = new ValeurMarcheVisitor(variableVisitor);

  UnitTestVisitor visitor = new UnitTestVisitor() {
    @Override
    public ValeurMarche visitValeurMarche(PatriLangParser.ValeurMarcheContext ctx) {
      return subject.apply(ctx);
    }
  };

  @Test
  void parse_valeur_marche() {
    Materiel possession = new Materiel("maisonTest", AJD, AJD, new Argent(1_000_000, Devise.MGA), 2.0);
    variableVisitor.addToScope("possessionTest", TRESORERIES, possession);
    variableVisitor.addToScope("ajd", DATE, AJD);

    var input = "* `valeurMarche` Dates:ajd, Trésoreries:possessionTest valant 1000000Ar";

    ValeurMarche expected = new ValeurMarche(possession, AJD, ariary(1_000_000));
    ValeurMarche actual = visitor.visit(input, PatriLangParser::valeurMarche);

    assertEquals(expected.t(), actual.t());
    assertEquals(expected.valeur().getMontant(), actual.valeur().getMontant());
    assertEquals(expected.possession().nom(), actual.possession().nom());
  }
}