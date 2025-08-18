package school.hei.patrimoine.patrilang.unit.visitors;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.MATERIEL;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class MaterielReferenceTest {

//    private VariableVisitor variableVisitor;
//    private Materiel monOrdi;
//    private static final LocalDate AJD = LocalDate.of(2025, 7, 29);
//
//    @BeforeEach
//    void setUp() {
//        variableVisitor = new VariableVisitor();
//        monOrdi = new Materiel("ordinateur", AJD, AJD,
//                ariary(200_000), 0.5);
//        // On met l'objet dans le scope AVANT le parsing
//        variableVisitor.addToScope("ordinateur", MATERIEL, monOrdi);
//    }
//
//    @Test
//    void should_parse_and_return_existing_materiel_from_scope() {
//        String input = "Materiel:ordinateur";
//
//        UnitTestVisitor visitor = new UnitTestVisitor() {
//            @Override
//            public Materiel visitMaterielReference(PatriLangParser.MaterielReferenceContext ctx) {
//                return variableVisitor.asMateriel(ctx);
//            }
//        };
//
//        Materiel actual = visitor.visit(input, PatriLangParser::materielReference);
//
//        assertNotNull(actual, "Le matériel référencé ne doit pas être null.");
//        assertSame(monOrdi, actual,
//                "On doit récupérer exactement le même objet que celui présent dans le scope.");
//    }
}
