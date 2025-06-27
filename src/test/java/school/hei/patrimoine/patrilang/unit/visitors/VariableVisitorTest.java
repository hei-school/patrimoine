package school.hei.patrimoine.patrilang.unit.visitors;

import static java.time.Month.FEBRUARY;
import static java.time.Month.JULY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;

import java.time.LocalDate;
import org.antlr.v4.runtime.tree.TerminalNode;
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
  private final VariableContext variableContextMock = mock(VariableContext.class);
  private final TerminalNode terminalNodeMock = mock(TerminalNode.class);

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
    when(variableContextMock.VARIABLE()).thenReturn(terminalNodeMock);
  }

  @Test
  void can_cast_variable_to_expected_type() {
    var expected = new Compte("compte", LocalDate.MAX, ariary(500_000));

    subject.addToScope(expected.nom(), TRESORERIES, expected);

    when(terminalNodeMock.getText()).thenReturn("Trésoreries:compte");

    var actual = subject.asCompte(variableContextMock);

    assertEquals(expected, actual);
  }

  @Test
  void throws_if_wrong_type_cast() {
    var personne = new Personne("Billy");

    subject.addToScope(personne.nom(), PERSONNE, personne);

    when(terminalNodeMock.getText()).thenReturn("Personnes:Jean");

    assertThrows(IllegalArgumentException.class, () -> subject.asCompte(variableContextMock));
  }

  @Test
  void should_handle_personnes_morale() {
    var familleRakoto = new PersonneMorale("FamilleRakoto");

    subject.addToScope(familleRakoto.nom(), PERSONNE_MORALE, familleRakoto);

    when(terminalNodeMock.getText()).thenReturn("PersonnesMorales:FamilleRakoto");

    var actual = subject.asPersonne(variableContextMock);

    assertEquals(familleRakoto.personne(), actual);
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
  void can_parse_variable_with_full_delta() {
    var input = "Dates:ajd + 2 années et 3mois et 4jours";
    var baseDate = LocalDate.of(2026, JULY, 13);
    var expected = baseDate.plusYears(2).plusMonths(3).plusDays(4);

    subject.addToScope("ajd", DATE, baseDate);

    var variable = (Variable<LocalDate>) visitor.visit(input, PatriLangParser::variable);
    var actual = variable.value();

    assertEquals(expected, actual);
  }

  @Test
  void throws_if_date_delta_provided_but_type_not_date() {
    var input = "Trésoreries:compte + 2 années et 3mois et 4jours";
    var baseDate = LocalDate.of(2026, JULY, 13);
    var expected = baseDate.plusYears(2).plusMonths(3).plusDays(4);

    subject.addToScope("compte", TRESORERIES, baseDate);

    var error =
        assertThrows(
            IllegalArgumentException.class, () -> visitor.visit(input, PatriLangParser::variable));

    assertEquals(
        "Le type attendu est : DATE, mais le type trouvé est : TRESORERIES.", error.getMessage());
  }
}
