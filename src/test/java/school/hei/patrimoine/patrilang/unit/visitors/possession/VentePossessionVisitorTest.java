package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.VenteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;
import school.hei.patrimoine.modele.vente.VentePossession;

class VentePossessionVisitorTest {
    private static final LocalDate AJD = LocalDate.of(2025, 7, 27);
    private static final VariableVisitor variableVisitor = new VariableVisitor();
    private static final Compte MON_COMPTE = new Compte("monCompte", AJD, ariary(500_000));
    private static final Materiel MON_ORDI = new Materiel("monOrdi", AJD, AJD, ariary(200_000), 0.0);
    private final VenteVisitor subject = new VenteVisitor(variableVisitor);

    private final UnitTestVisitor visitor =
            new UnitTestVisitor() {
                @Override
                public VentePossession visitVentePossession(PatriLangParser.VentePossessionContext ctx) {
                    return subject.apply(ctx);
                }
            };

    static {
        variableVisitor.addToScope("ajd", DATE, AJD);
        variableVisitor.addToScope("monCompte", TRESORERIES, MON_COMPTE);
        variableVisitor.addToScope("monOrdi", IMMOBILISATION, MON_ORDI);
    }

    @Test
    void parse_vente_possession_should_mark_possession_as_sold_and_credit_compte() {
        var input =
                """
                * Vente `vente1`, le ajd, vente de monOrdi à 250000 Ar pour compte monCompte
                """;

        var vente = (VentePossession) visitor.visit(input, PatriLangParser::ventePossession);
        vente.execute();

        assertTrue(MON_ORDI.estVendue());
        assertEquals(AJD, MON_ORDI.getDateVente());
        assertEquals(ariary(250_000), MON_ORDI.getPrixVente());
        assertEquals(MON_COMPTE, MON_ORDI.getCompteBeneficiaire());
        assertEquals(ariary(750_000), MON_COMPTE.getSolde());
    }

    @Test
    void parse_vente_possession_deja_vendue_should_throw_exception() {
        MON_ORDI.vendre(AJD, ariary(100_000), MON_COMPTE);

        var input =
                """
                * Vente `vente1`, le ajd, vente de monOrdi à 250000 Ar pour compte monCompte
                """;

        var vente = (VentePossession) visitor.visit(input, PatriLangParser::ventePossession);

        var exception = assertThrows(IllegalStateException.class, vente::execute);

        assertEquals("Déjà vendue", exception.getMessage());
    }
}
