package school.hei.patrimoine.patrilang.unit.modele;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.patrilang.modele.VariableType.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.patrilang.modele.Variable;
import school.hei.patrimoine.patrilang.modele.VariableContainer;

class VariableContainerTest {
  private VariableContainer subject;

  @BeforeEach
  void setUp() {
    subject = new VariableContainer();
  }

  @Test
  void can_add_and_get_variable() {
    var expected = new Variable<>("nom", PERSONNE, new Personne("Jean"));

    subject.add(expected);

    var actual = subject.get("nom", PERSONNE);

    assertEquals(expected, actual);
  }

  @Test
  void add_same_name_and_type_throws_exception() {
    var v1 = new Variable<>("nom", PERSONNE, "Jean");
    var v2 = new Variable<>("nom", PERSONNE, "Marie");

    subject.add(v1);

    var error = assertThrows(IllegalArgumentException.class, () -> subject.add(v2));

    assertTrue(error.getMessage().contains("déjà été définie"));
  }

  @Test
  void get_throws_exception_if_variable_not_found() {
    var error =
        assertThrows(IllegalArgumentException.class, () -> subject.get("inexistant", DETTE));

    assertTrue(error.getMessage().contains("n'existe pas"));
  }
}
