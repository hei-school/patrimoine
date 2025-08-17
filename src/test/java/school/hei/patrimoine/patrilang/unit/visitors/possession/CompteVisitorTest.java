
package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static java.time.Month.JANUARY;
import static java.util.Calendar.FEBRUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.CompteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class CompteVisitorTest {
    CompteVisitor subject = new CompteVisitor(new VariableVisitor());

    UnitTestVisitor visitor =
            new UnitTestVisitor() {
                @Override
                public Compte visitCompte(CompteContext ctx) {
                    return subject.apply(ctx);
                }
            };

    @Test
    void parse_normal_compte() {
        var input = "monCompte, valant 300000Ar le 01 du 01-2025";

        var expected = new Compte("monCompte", LocalDate.of(2025, JANUARY, 1), ariary(300_000));

        Compte actual = visitor.visit(input, PatriLangParser::compte);

        assertEquals(expected.nom(), actual.nom());
        assertEquals(expected.getDateOuverture(), actual.getDateOuverture());
        assertEquals(expected.valeurComptable(), actual.valeurComptable());
    }
    @Test
    void parse_compte_different_date() {

        String input = "monAutreCompte, valant 150000Ar le 15 du 02-2025";
        Compte expected = new Compte("monAutreCompte", LocalDate.of(2025, 2, 15), ariary(150_000));
        Compte actual = visitor.visit(input, PatriLangParser::compte);
        assertEquals(expected.nom(), actual.nom());
        assertEquals(expected.getDateOuverture(), actual.getDateOuverture());
        assertEquals(expected.valeurComptable(), actual.valeurComptable());
    }

    @Test
    void parse_compte_valeur_decimal() {
        String input = "compteDecimal, valant 12345.67Ar le 10 du 01-2025";

        Compte expected = new Compte("compteDecimal", LocalDate.of(2025, JANUARY, 10), new school.hei.patrimoine.modele.Argent(12345.67, ariary(0).devise()));

        Compte actual = visitor.visit(input, PatriLangParser::compte);

        assertEquals(expected.nom(), actual.nom());
        assertEquals(expected.getDateOuverture(), actual.getDateOuverture());
        assertEquals(expected.valeurComptable().ppMontant(), actual.valeurComptable().ppMontant());
    }
}
