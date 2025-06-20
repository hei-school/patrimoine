package school.hei.patrimoine.patrilang.unit.visitors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

import java.time.LocalDate;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.patrilang.visitors.DateVisitor;
import school.hei.patrimoine.patrilang.visitors.VariableVisitor;

class DateVisitorTest {
  DateVisitor subject;
  VariableVisitor variableVisitor;
  DateContext dateContextMock;

  @BeforeEach
  void setUp() {
    dateContextMock = mock();
    variableVisitor = new VariableVisitor();
    subject = new DateVisitor(variableVisitor);
  }

  @Test
  void can_parse_date_indeterminate() {
    var expected = LocalDate.MAX;

    var terminalNodeMock = terminalNode("date indéterminée");
    when(dateContextMock.MOT_DATE_INDETERMINER()).thenReturn(terminalNodeMock);

    var actual = subject.apply(dateContextMock);

    assertEquals(expected, actual);
  }

  @Test
  void can_parse_normal_date() {
    var expected = LocalDate.of(2025, 2, 1);

    when(dateContextMock.MOT_DATE_INDETERMINER()).thenReturn(null);
    when(dateContextMock.dateExpr()).thenReturn(null);

    dateContextMock.annee = token("2025");
    dateContextMock.mois = token("02");
    dateContextMock.jour = token("01");

    var actual = subject.apply(dateContextMock);

    assertEquals(expected, actual);
  }

  @Test
  void can_parse_variable_without_delta() {
    var expected = LocalDate.of(2025, 10, 27);
    variableVisitor.addToScope("ajd", DATE, expected);

    var dateExprMock = mock(DateExprContext.class);
    when(dateContextMock.MOT_DATE_INDETERMINER()).thenReturn(null);
    when(dateContextMock.dateExpr()).thenReturn(dateExprMock);
    when(dateExprMock.MOINS()).thenReturn(null);
    when(dateExprMock.dateDelta()).thenReturn(null);

    var variableContextMock = mock(VariableContext.class);
    when(dateExprMock.variable()).thenReturn(variableContextMock);

    var terminalNodeMock = terminalNode("Dates:ajd");
    when(variableContextMock.VARIABLE()).thenReturn(terminalNodeMock);

    var actual = subject.apply(dateContextMock);

    assertEquals(expected, actual);
  }

  @Test
  void can_parse_variable_with_full_delta() {
    var baseDate = LocalDate.of(2026, 7, 13);
    var expected = baseDate.plusYears(2).plusMonths(3).plusDays(4);
    variableVisitor.addToScope("ajd", DATE, baseDate);

    var dateExprMock = mock(DateExprContext.class);
    when(dateContextMock.MOT_DATE_INDETERMINER()).thenReturn(null);
    when(dateContextMock.dateExpr()).thenReturn(dateExprMock);
    when(dateExprMock.MOINS()).thenReturn(null);
    when(dateExprMock.PLUS()).thenReturn(mock());

    var deltaContextMock = mock(DateDeltaContext.class);
    when(dateExprMock.dateDelta()).thenReturn(deltaContextMock);

    var annePartContextMock = mock(AnneePartContext.class);
    var anneeMockValue = terminalNode("2");
    var moisPartContextMock = mock(MoisPartContext.class);
    var moisMockValue = terminalNode("3");
    var jourPartContextMock = mock(JourPartContext.class);
    var jourMockValue = terminalNode("4");

    when(annePartContextMock.ENTIER()).thenReturn(anneeMockValue);
    when(moisPartContextMock.ENTIER()).thenReturn(moisMockValue);
    when(jourPartContextMock.ENTIER()).thenReturn(jourMockValue);
    when(deltaContextMock.anneePart()).thenReturn(annePartContextMock);
    when(deltaContextMock.moisPart()).thenReturn(moisPartContextMock);
    when(deltaContextMock.jourPart()).thenReturn(jourPartContextMock);

    var variableContextMock = mock(VariableContext.class);
    when(dateExprMock.variable()).thenReturn(variableContextMock);

    var terminalNodeMock = terminalNode("Dates:ajd");
    when(variableContextMock.VARIABLE()).thenReturn(terminalNodeMock);

    var actual = subject.apply(dateContextMock);

    assertEquals(expected, actual);
  }

  private Token token(String value) {
    var tokenMock = mock(Token.class);

    when(tokenMock.getText()).thenReturn(value);

    return tokenMock;
  }

  private TerminalNode terminalNode(String value) {
    var terminalNode = mock(TerminalNode.class);

    when(terminalNode.getText()).thenReturn(value);

    return terminalNode;
  }
}
