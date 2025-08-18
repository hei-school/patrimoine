package school.hei.patrimoine.patrilang.unit.visitors;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.MATERIEL;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.patrilang.modele.variable.Variable;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class VariableVisitorMaterielTest {

    private VariableVisitor variableVisitor;
    private Materiel monOrdi;
    private static final LocalDate AJD = LocalDate.of(2025, 7, 29);

    @BeforeEach
    void setUp() {
        variableVisitor = new VariableVisitor();
        monOrdi = new Materiel("ordinateur", AJD, AJD,
                ariary(200_000), 0.5);
        variableVisitor.addToScope("ordinateur", MATERIEL, monOrdi);
    }

    @Test
    void materiel_should_be_stored_and_retrievable_in_scope() {
        Variable<?> retrieved = variableVisitor.getVariableScope().get("ordinateur", MATERIEL);
        assertNotNull(retrieved, "La variable 'ordinateur' devrait exister dans le scope.");

        assertTrue(retrieved.value() instanceof Materiel,
                "La valeur devrait être de type Materiel.");

        assertSame(monOrdi, retrieved.value(),
                "L'objet récupéré devrait être exactement le même que celui inséré.");
    }
}
