package school.hei.patrimoine.patrilang.unit.visitors.possession;

import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.ValeurMarche.ValeurMarche;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Entreprise;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.CompteVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.ValeurMarcheVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.patrimoine.modele.possession.TypeAgregat.ENTREPRISE;
import static school.hei.patrimoine.modele.possession.TypeAgregat.TRESORERIE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;


import java.time.LocalDate;

import static java.time.Month.JANUARY;
import school.hei.patrimoine.modele.possession.Materiel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;

public class ValeurMarcheVisitorTest {

    private VariableVisitor variableVisitor;
    private ValeurMarcheVisitor subject;
    private UnitTestVisitor visitor;

    private static final Compte COMPTE_A_VENDRE = new Compte("compte1", LocalDate.of(2023, 6, 2), ariary(50_000));

    @BeforeEach
    void setup() {
        variableVisitor = new VariableVisitor();
        subject = new ValeurMarcheVisitor(variableVisitor);

        visitor = new UnitTestVisitor() {
            @Override
            public ValeurMarche visitValeurMarche(PatriLangParser.ValeurMarcheContext ctx) {
                return subject.apply(ctx);
            }
        };

        // Injection de la date "ajd"
        variableVisitor.addToScope("ajd", DATE, LocalDate.of(2025, 6, 23));

        // Injection d'un compte dans la portée avec type TRESORERIES
        variableVisitor.addToScope("compte1", TRESORERIES, COMPTE_A_VENDRE);
    }

    @Test
    void adding_valeur_marche_to_compte_should_throw_exception() {
        String input = """
            * `valeurMarche1`, le `ajd`, valeur marché de 300000Ar pour `compte1`
        """;

        UnsupportedOperationException thrown = assertThrows(
                UnsupportedOperationException.class,
                () -> visitor.visit(input, PatriLangParser::valeurMarche)
        );

        assertEquals(
                "Seules les possessions de type IMMOBILISATION ou ENTREPRISE peuvent avoir une valeur de marché.",
                thrown.getMessage()
        );
    }
}