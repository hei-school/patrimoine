package school.hei.patrimoine.patrilang.unit.visitors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.PERSONNE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;

import java.time.LocalDate;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

class VariableVisitorTest {
  private VariableVisitor subject;
  private final VariableContext variableContextMock = mock(VariableContext.class);
  private final TerminalNode terminalNodeMock = mock(TerminalNode.class);

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
}
