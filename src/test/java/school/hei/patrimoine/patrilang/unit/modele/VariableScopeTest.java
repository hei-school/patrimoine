package school.hei.patrimoine.patrilang.unit.modele;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.PERSONNE;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;

class VariableScopeTest {
  VariableScope subject;

  @Test
  void throws_if_variable_not_found() {
    subject = new VariableScope(Optional.empty());

    var error = assertThrows(IllegalArgumentException.class, () -> subject.get("notfound", DATE));

    assertTrue(error.getMessage().contains("n'existe pas"));
  }

  @Test
  void get_from_parent_scope_if_variable_not_found_from_current_scope() {
    var expected = new Personne("John");
    var parentScope = new VariableScope(Optional.empty());
    subject = new VariableScope(Optional.of(parentScope));

    parentScope.add("john", PERSONNE, expected);

    var actual = subject.get("john", PERSONNE).value();

    assertEquals(expected, actual);
  }

  @Test
  void get_from_current_scope() {
    var john1 = new Personne("John1");
    var john2 = new Personne("John2");
    var parentScope = new VariableScope(Optional.empty());
    subject = new VariableScope(Optional.of(parentScope));

    parentScope.add("john", PERSONNE, john1);
    subject.add("john", PERSONNE, john2);

    var actual = subject.get("john", PERSONNE).value();

    assertNotEquals(john1, actual);
    assertEquals(john2, actual);
  }
}
