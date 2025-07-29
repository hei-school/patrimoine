package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VentePossessionContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.vente.VentePossession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.VentePossessionVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class VentePossessionVisitorTest {

  private static final LocalDate AJD = LocalDate.of(2025, 7, 27);

  private VariableVisitor variableVisitor;
  private VentePossessionVisitor subject;
  private UnitTestVisitor visitor;

  private Compte monCompte;
  private Materiel monOrdi;

  @BeforeEach
  void setUp() {
    variableVisitor = new VariableVisitor();
    subject = new VentePossessionVisitor(variableVisitor);
    visitor =
        new UnitTestVisitor() {
          @Override
          public VentePossession visitVentePossession(VentePossessionContext ctx) {
            return subject.apply(ctx);
          }
        };

    monCompte = new Compte("monCompte", AJD, ariary(500_000));
    monOrdi = new Materiel("monOrdi", AJD, AJD, ariary(200_000), 0.5);

    variableVisitor.addToScope("ajd", DATE, AJD);
    variableVisitor.addToScope("monCompte", TRESORERIES, monCompte);
    variableVisitor.addToScope("monOrdi", IMMOBILISATION, monOrdi);
  }

  @Test
  void parse_vente_possession_should_mark_possession_as_sold_and_credit_compte() {
    var input =
        """
* `vente1`, vendre Immobilisations:monOrdi à 250000Ar le Dates:ajd verser dans Trésoreries:monCompte
""";

    var vente = (VentePossession) visitor.visit(input, PatriLangParser::ventePossession);
    vente.execute();

    assertTrue(monOrdi.estVendue());
    assertEquals(AJD, monOrdi.getDateVente());
    assertEquals(ariary(250_000), monOrdi.getPrixVente());
    assertEquals(monCompte, monOrdi.getCompteBeneficiaire());
    assertEquals(ariary(750_000), monCompte.getSolde());
  }

  @Test
  void parse_vente_possession_deja_vendue_should_throw_exception() {
    monOrdi.vendre(AJD, ariary(100_000), monCompte);

    var input =
        """
* `vente2`, vendre Immobilisations:monOrdi à 250000Ar le Dates:ajd verser dans Trésoreries:monCompte
""";

    var vente = (VentePossession) visitor.visit(input, PatriLangParser::ventePossession);

    var exception = assertThrows(IllegalStateException.class, vente::execute);

    assertEquals("Déjà vendue", exception.getMessage());
  }
}
