package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static java.time.Month.JUNE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.RemboursementDette;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.RemboursementDetteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class RemboursementDetteVisitorTest {
  private static final LocalDate AJD = LocalDate.of(2025, JUNE, 23);
  private static final VariableVisitor variableVisitor = new VariableVisitor();
  private static final IdVisitor idVisitor = new IdVisitor(variableVisitor);
  private static final Compte COMPTE1 = new Compte("Trésoreries:dummyCompte", AJD, ariary(0));
  private static final Dette DETTES = new Dette("Dettes:detteCompte", AJD, ariary(-1000));
  private static final Compte COMPTE2 = new Compte("Trésorerie:kotoCompte", AJD, ariary(0));
  private static final Creance CREANCES = new Creance("Créances:creanceCompte", AJD, ariary(500));
  private static final RemboursementDetteVisitor subject =
      new RemboursementDetteVisitor(variableVisitor, idVisitor);

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public RemboursementDette visitRembourserDette(PatriLangParser.RembourserDetteContext ctx) {
          return subject.apply(ctx);
        }
      };

  static {
    variableVisitor.addToScope("dummyCompte", TRESORERIES, COMPTE1);
    variableVisitor.addToScope("kotoCompte", TRESORERIES, COMPTE2);
    variableVisitor.addToScope("creanceCompte", CREANCE, CREANCES);
    variableVisitor.addToScope("detteCompte", DETTE, DETTES);
  }

  @Test
  void parser_remboursement_dette_simple() {
    var input =
        "* `remboursementDette`, le 23 juin 2025, rembourser Dettes:detteCompte de"
            + " Trésoreries:dummyCompte avec Créances:creanceCompte de Trésoreries:kotoCompte"
            + " valant 15000Ar";

    var expected =
        new RemboursementDette(
            "remboursementDette", COMPTE1, COMPTE2, DETTES, CREANCES, AJD, ariary(15_000));

    var actual = visitor.visit(input, PatriLangParser::rembourserDette);

    assertEquals(expected, actual);
  }

  @Test
  void parser_remboursement_dette_avec_valeur_negatif() {
    var input =
        "* `remboursementDette`, le 23 juin 2025, rembourser Dettes:detteCompte de"
            + " Trésoreries:dummyCompte avec Créances:creanceCompte de Trésoreries:kotoCompte"
            + " valant -5000Ar";

    var expected =
        new RemboursementDette(
            "remboursementDette", COMPTE1, COMPTE2, DETTES, CREANCES, AJD, ariary(-5_000));

    var actual = visitor.visit(input, PatriLangParser::rembourserDette);

    assertEquals(expected, actual);
  }
}
