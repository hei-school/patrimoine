package school.hei.patrimoine.patrilang.unit.visitors.possession.vente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.MATERIEL;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.vente.ValeurMarche;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.vente.ValeurMarcheVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class ValeurMarcheVisitorTest {
  private static final LocalDate T0 = LocalDate.of(2025, 1, 1);
  private static final LocalDate T_VM = LocalDate.of(2025, 6, 1);

  private static final VariableVisitor variableVisitor = new VariableVisitor();
  private static final Materiel VOITURE = new Materiel("voiture", T0, T0, ariary(10_000_000), -0.1);

  ValeurMarcheVisitor subject = new ValeurMarcheVisitor(variableVisitor);

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public ValeurMarche visitValeurMarche(PatriLangParser.ValeurMarcheContext ctx) {
          return subject.apply(ctx);
        }
      };

  static {
    variableVisitor.addToScope("t0", DATE, T0);
    variableVisitor.addToScope("tVm", DATE, T_VM);
    variableVisitor.addToScope("voiture", MATERIEL, VOITURE);
  }

  @Test
  void parse_valeur_marche_avec_date_variable() {
    var input = "* `vmVoiture`, Dates:tVm Matériel:voiture valant 12000000Ar";

    ValeurMarche actual = visitor.visit(input, PatriLangParser::valeurMarche);

    assertEquals(VOITURE, actual.possession());
    assertEquals(T_VM, actual.t());
    assertEquals(ariary(12_000_000), actual.valeur());
  }

  @Test
  void parse_valeur_marche_avec_date_litterale() {
    var input = "* `vmVoiture2`, le 01 juin 2025 Matériel:voiture valant 15000000Ar";

    ValeurMarche actual = visitor.visit(input, PatriLangParser::valeurMarche);

    assertEquals(VOITURE, actual.possession());
    assertEquals(T_VM, actual.t());
    assertEquals(ariary(15_000_000), actual.valeur());
  }

  @Test
  void parse_valeur_marche_est_ajoutee_a_la_possession() {
    var input = "* `vmVoiture3`, Dates:tVm Matériel:voiture valant 11000000Ar";

    ValeurMarche actual = visitor.visit(input, PatriLangParser::valeurMarche);
    assertEquals(actual.valeur(), VOITURE.getValeurMarche(T_VM));
  }
}
