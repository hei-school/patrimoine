package school.hei.patrimoine.patrilang.unit.visitors;

import static java.time.Month.FEBRUARY;
import static java.time.Month.JULY;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.PersonneMorale;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.modele.variable.Variable;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@SuppressWarnings("all")
class VariableVisitorTest {
  VariableVisitor subject;

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public Variable<?> visitVariable(VariableContext ctx) {
          return subject.apply(ctx);
        }
      };

  @BeforeEach
  void setUp() {
    subject = new VariableVisitor();
  }

  @Test
  void can_cast_variable_to_expected_type() {
    var input = "Trésoreries:compte";
    var expected = new Compte("compte", LocalDate.MAX, ariary(500_000));

    subject.addToScope(expected.nom(), TRESORERIES, expected);

    var actual = (Variable<?>) visitor.visit(input, PatriLangParser::variable);

    assertEquals(expected, actual.value());
  }

  @Test
  void throws_if_wrong_type_cast() {
    var input = "Personnes:Jean";
    var personne = new Personne("Billy");

    subject.addToScope(personne.nom(), PERSONNE, personne);

    assertThrows(
        IllegalArgumentException.class, () -> visitor.visit(input, PatriLangParser::variable));
  }

  @Test
  void should_handle_personnes_morale() {
    var input = "PersonnesMorales:FamilleRakoto";
    var familleRakoto = new PersonneMorale("FamilleRakoto");

    subject.addToScope(familleRakoto.nom(), PERSONNE_MORALE, familleRakoto);

    var actual = (Variable<PersonneMorale>) visitor.visit(input, PatriLangParser::variable);

    assertEquals(familleRakoto.personne(), actual.value().personne());
  }

  @Test
  void parse_normal_date() {
    var input = "le 01 du 02-2025";
    var expected = LocalDate.of(2025, FEBRUARY, 1);

    var variable = (Variable<LocalDate>) visitor.visit(input, PatriLangParser::variable);
    var actual = variable.value();

    assertEquals(expected, actual);
  }

  @Test
  void parse_normal_date_textuel() {
    var input = "le 01 février 2025";
    var expected = LocalDate.of(2025, FEBRUARY, 1);

    var variable = (Variable<LocalDate>) visitor.visit(input, PatriLangParser::variable);
    var actual = variable.value();

    assertEquals(expected, actual);
  }

  @Test
  void parse_normal_date_with_variable() {
    var input = "le Nombres:jour du Nombres:mois-Nombres:annee";
    var expected = LocalDate.of(2025, FEBRUARY, 1);

    subject.addToScope("jour", NOMBRE, (double) expected.getDayOfMonth());
    subject.addToScope("mois", NOMBRE, (double) expected.getMonth().getValue());
    subject.addToScope("annee", NOMBRE, (double) expected.getYear());

    var variable = (Variable<LocalDate>) visitor.visit(input, PatriLangParser::variable);
    var actual = variable.value();

    assertEquals(expected, actual);
  }

  @Test
  void parse_normal_date_textuel_with_variable() {
    var input = "le Nombres:premierDuMois février Nombres:anneeRecap";
    var expected = LocalDate.of(2025, FEBRUARY, 1);

    subject.addToScope("premierDuMois", NOMBRE, (double) expected.getDayOfMonth());
    subject.addToScope("anneeRecap", NOMBRE, (double) expected.getYear());

    var variable = (Variable<LocalDate>) visitor.visit(input, PatriLangParser::variable);
    var actual = variable.value();

    assertEquals(expected, actual);
  }

  @Test
  void can_parse_variable_with_full_delta() {
    var input = "Dates:ajd + 2 années et 3mois et 4jours";
    var baseDate = LocalDate.of(2026, JULY, 13);
    var expected = baseDate.plusYears(2).plusMonths(3).plusDays(4);

    subject.addToScope("ajd", DATE, baseDate);

    var variable = (Variable<LocalDate>) visitor.visit(input, PatriLangParser::variable);
    var actual = variable.value();

    assertEquals(expected, actual);
  }
}
