package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.vente.Vente;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.VenteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class VenteVisitorTest {
  private static final LocalDate AJD = LocalDate.of(2025, 7, 27);
  private VariableVisitor variableVisitor;
  private VenteVisitor subject;
  private UnitTestVisitor visitor;
  private Compte monCompte;
  private Compte monCompte1;

  @BeforeEach
  void setUp() {
    variableVisitor = new VariableVisitor();
    subject = new VenteVisitor(variableVisitor);

    visitor =
            new UnitTestVisitor() {
              @Override
              public Vente visitVente(PatriLangParser.VenteContext ctx) {
                return subject.apply(ctx);
              }
            };

    monCompte = new Compte("monCompte", AJD, ariary(500_000));
    monCompte1 = new Compte("monCompte1", AJD, ariary(50_000));

    variableVisitor.addToScope("ajd", DATE, AJD);
    variableVisitor.addToScope("monCompte", TRESORERIES, monCompte);
    variableVisitor.addToScope("monCompte1", TRESORERIES, monCompte1);
  }

  @Test
  void parse_vente_compte_should_return_vente_correctly() {
    var input =
            """  
    * `vente1`, vendre Trésoreries:monCompte1 à 250000Ar le Dates:ajd verser dans Trésoreries:monCompte  
    """;

    var result = visitor.visit(input, PatriLangParser::vente);

    assertTrue(monCompte1.estVendue());
    assertEquals(AJD, monCompte1.getDateVente());
    assertEquals(ariary(250_000), monCompte1.getPrixVente());
    assertEquals(monCompte, monCompte1.getCompteBeneficiaire());
    assertEquals(ariary(750_000), monCompte.valeurActuelle());
  }

  @Test
  void parse_vente_possession_deja_vendue_should_throw_exception() {
    var input =
            """  
    * `vente1`, vendre Trésoreries:monCompte1 à 250000Ar le Dates:ajd verser dans Trésoreries:monCompte  
    """;

    visitor.visit(input, PatriLangParser::vente);

    var exception =
            assertThrows(
                    IllegalStateException.class, () -> visitor.visit(input, PatriLangParser::vente));

    assertEquals(
            "La possession 'monCompte1' est déjà vendue le 2025-07-27", exception.getMessage());
  }
  @Test
  void multiple_ventes_update_beneficiary_value_correctly() {
    var input1 =
            """  
            * `vente1`, vendre Trésoreries:monCompte1 à 250000Ar le Dates:ajd verser dans Trésoreries:monCompte            """;
    var input2 =
            """  
            * `vente2`, vendre Trésoreries:monCompte1 à 100000Ar le Dates:ajd verser dans Trésoreries:monCompte            """;

    visitor.visit(input1, PatriLangParser::vente);

    assertThrows(IllegalStateException.class,
            () -> visitor.visit(input2, PatriLangParser::vente));

    assertEquals(ariary(750_000), monCompte.valeurActuelle());
  }
}





