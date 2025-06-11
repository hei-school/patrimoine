package school.hei.patrimoine.patrilang.unit.visitors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableValueContext;
import static school.hei.patrimoine.patrilang.modele.VariableType.PERSONNE;
import static school.hei.patrimoine.patrilang.modele.VariableType.TRESORERIES;

import java.time.LocalDate;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.modele.*;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

class VariableVisitorTest {
  private VariableContainer container;
  private VariableVisitor visitor;

  private final VariableContext variableContextMock = mock(VariableContext.class);
  private final VariableValueContext variableValueContextMock = mock(VariableValueContext.class);
  private final TerminalNode terminalNodeMock = mock(TerminalNode.class);

  @BeforeEach
  void setUp() {
    container = new VariableContainer();
    visitor = new VariableVisitor(container);

    when(variableContextMock.variableValue()).thenReturn(variableValueContextMock);
    when(variableValueContextMock.VARIABLE()).thenReturn(terminalNodeMock);
  }

  @Test
  void can_cast_variable_to_expected_type() {
    var expected = new Compte("compte", LocalDate.MAX, ariary(500_000));
    var compteVariable = new Variable<>(expected.nom(), TRESORERIES, expected);

    container.add(compteVariable);

    when(terminalNodeMock.getText()).thenReturn("Trésoreries:compte");

    var actual = visitor.asCompte(variableContextMock);

    assertEquals(expected, actual);
  }

  @Test
  void throws_if_wrong_type_cast() {
    var personne = new Personne("Billy");
    var personneVariable = new Variable<>(personne.nom(), PERSONNE, personne);

    container.add(personneVariable);

    when(terminalNodeMock.getText()).thenReturn("Personnes:Jean");

    assertThrows(IllegalArgumentException.class, () -> visitor.asCompte(variableContextMock));
  }
}
