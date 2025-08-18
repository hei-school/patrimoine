package school.hei.patrimoine.patrilang.unit.modele;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.MATERIEL;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;

class VariableScopeMaterielTest {
    VariableScope subject;

    @Test
    void throws_if_materiel_not_found() {
        subject = new VariableScope(Optional.empty());

        var error = assertThrows(
                IllegalArgumentException.class,
                () -> subject.get("ordinateur", MATERIEL)
        );

        assertTrue(error.getMessage().contains("n'existe pas"));
    }

    @Test
    void get_materiel_from_parent_scope_if_not_found_in_current() {
        var expected = new Materiel(
                "ordinateur",
                LocalDate.of(2025, 1, 10),
                LocalDate.of(2025, 1, 10),
                ariary(200_000),
                0.01
        );

        var parentScope = new VariableScope(Optional.empty());
        subject = new VariableScope(Optional.of(parentScope));

        parentScope.add("ordinateur", MATERIEL, expected);

        var actual = subject.get("ordinateur", MATERIEL).value();

        assertEquals(expected, actual);
    }

    @Test
    void get_materiel_from_current_scope_even_if_parent_has_same_name() {
        var materiel1 = new Materiel(
                "ordinateur",
                LocalDate.of(2025, 1, 10),
                LocalDate.of(2025, 1, 10),
                ariary(200_000),
                0.01
        );

        var materiel2 = new Materiel(
                "ordinateur",
                LocalDate.of(2025, 1, 15),
                LocalDate.of(2025, 1, 15),
                ariary(300_000),
                0.02
        );

        var parentScope = new VariableScope(Optional.empty());
        subject = new VariableScope(Optional.of(parentScope));

        parentScope.add("ordinateur", MATERIEL, materiel1);
        subject.add("ordinateur", MATERIEL, materiel2);

        var actual = subject.get("ordinateur", MATERIEL).value();

        assertNotEquals(materiel1, actual);
        assertEquals(materiel2, actual);
    }
}
