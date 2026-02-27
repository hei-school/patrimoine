package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.TypeComptable.IMMOBILISATION;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.PossedeMaterielContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.MaterielVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class MaterielVisitorTest {
  private static final LocalDate AJD = LocalDate.of(2025, 6, 23);
  private static final VariableVisitor variableVisitor = new VariableVisitor();

  MaterielVisitor subject = new MaterielVisitor(variableVisitor);

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public OperationComptable visitPossedeMateriel(PossedeMaterielContext ctx) {
          return subject.apply(ctx);
        }
      };

  static {
    variableVisitor.addToScope("ajd", DATE, AJD);
  }

  @Test
  void parser_materiel_who_has_appreciation_positive() {
    var au1Janvier2026 = LocalDate.of(2026, 1, 1);
    var input =
        """
    * `possèdeMateriel`, Dates:ajd posséder ordinateur valant 200000Ar, s'appréciant annuellement de 1%
""";

    var expected = new Materiel("ordinateur", AJD, AJD, ariary(200_000), 0.01);

    OperationComptable actual = visitor.visit(input, PatriLangParser::possedeMateriel);

    assertEquals(expected.nom(), actual.possession().nom());
    assertEquals(expected.valeurComptable(), actual.possession().valeurComptable());
    assertEquals(
        expected.projectionFuture(au1Janvier2026).valeurComptable(),
        actual.possession().projectionFuture(au1Janvier2026).valeurComptable());
  }

  @Test
  void parser_materiel_who_has_appreciation_negative() {
    var au1Janvier2026 = LocalDate.of(2026, 1, 1);
    var input =
        """
    * `possèdeMateriel`, Dates:ajd posséder ordinateur valant 200000Ar, se dépréciant annuellement de 20%
""";

    var expected = new Materiel("ordinateur", AJD, AJD, ariary(200_000), -0.2);

    OperationComptable actual = visitor.visit(input, PatriLangParser::possedeMateriel);

    assertEquals(expected.nom(), actual.possession().nom());
    assertEquals(expected.valeurComptable(), actual.possession().valeurComptable());
    assertEquals(
        expected.projectionFuture(au1Janvier2026).valeurComptable(),
        actual.possession().projectionFuture(au1Janvier2026).valeurComptable());
  }

  @Test
  void posseder_materiel_has_default_type_immobilisation() {
    var input =
        """
    * `possedeMateriel` Dates:ajd, posséder Maison valant 50_000_000Ar Dates:ajd s'appréciant annuellement de 0%
""";

    OperationComptable actual = visitor.visit(input, PatriLangParser::possedeMateriel);

    assertInstanceOf(Materiel.class, actual.possession());
    assertEquals(IMMOBILISATION, actual.type());
  }

  @Test
  void posseder_materiel_reject_defined_type() {
    var input =
        """
    * `AUTRE possedeMateriel` Dates:ajd, posséder Maison valant 50_000_000Ar Dates:ajd s'appréciant annuellement de 0%
""";

    OperationComptable actual = visitor.visit(input, PatriLangParser::possedeMateriel);

    assertEquals(IMMOBILISATION, actual.type());
  }
}
