package school.hei.patrimoine.patrilang.unit.visitors.possession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Vente;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.VenteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;

public class VenteVisitorTest {
    private static final LocalDate AJD = LocalDate.now();
    private static final LocalDate DATE_FUTURE = AJD.plusYears(1);

    private VariableVisitor variableVisitor;
    private VenteVisitor subject;
    private UnitTestVisitor visitor;

    @BeforeEach
    void setUp() {
        variableVisitor = new VariableVisitor();
        subject = new VenteVisitor(variableVisitor);
        visitor = new UnitTestVisitor() {
            @Override
            public Vente visitVente(PatriLangParser.VenteContext ctx) {
                return subject.apply(ctx);
            }
        };
    }

    @Test
    void parse_vente_materiel() {
        Materiel materiel = new Materiel("voiture", AJD, AJD, ariary(10_000_000), 10.0);
        Compte compteBeneficiaire = new Compte("comptePrincipal", AJD, ariary(5_000_000));

        variableVisitor.addToScope("voiture", TRESORERIES, materiel);
        variableVisitor.addToScope("compte", TRESORERIES, compteBeneficiaire);
        variableVisitor.addToScope("ajd", DATE, AJD);

        var input = "* `vente` Dates:ajd, Trésoreries:voiture, Comptes:compte valant 8000000Ar";

        Vente actual = visitor.visit(input, PatriLangParser::vente);

        // Utilisation des getters Lombok standards
        assertEquals(AJD, actual.getTVente());
        assertEquals(materiel, actual.getPossession());
        assertEquals(compteBeneficiaire, actual.getCompteBeneficiaire());
        assertEquals(ariary(8_000_000), actual.getPrixVente());
    }

    @Test
    void parse_vente_compte() {
        Compte compteAVendre = new Compte("compteEpargne", AJD, ariary(2_000_000));
        Compte compteDestinataire = new Compte("comptePrincipal", AJD, ariary(5_000_000));

        variableVisitor.addToScope("epargne", TRESORERIES, compteAVendre);
        variableVisitor.addToScope("principal", TRESORERIES, compteDestinataire);
        variableVisitor.addToScope("ajd", DATE, AJD);

        var input = "* `vente` Dates:ajd, Comptes:epargne, Comptes:principal valant 2000000Ar";

        Vente actual = visitor.visit(input, PatriLangParser::vente);

        assertEquals(AJD, actual.getTVente());
        assertEquals(compteAVendre, actual.getPossession());
        assertEquals(compteDestinataire, actual.getCompteBeneficiaire());
        assertEquals(ariary(2_000_000), actual.getPrixVente());
    }

    @Test
    void parse_vente_date_future() {
        Materiel materiel = new Materiel("ordinateur", AJD, AJD, ariary(1_500_000), 3.0);
        Compte compte = new Compte("compteSecondaire", AJD, ariary(500_000));

        variableVisitor.addToScope("ordi", TRESORERIES, materiel);
        variableVisitor.addToScope("secondaire", TRESORERIES, compte);
        variableVisitor.addToScope("future", DATE, DATE_FUTURE);

        var input = "* `vente` Dates:future, Trésoreries:ordi, Comptes:secondaire valant 1000000Ar";

        Vente actual = visitor.visit(input, PatriLangParser::vente);

        assertEquals(DATE_FUTURE, actual.getTVente());
    }
}