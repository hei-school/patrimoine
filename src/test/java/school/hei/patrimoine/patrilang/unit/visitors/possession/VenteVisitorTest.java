package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Vente;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.VenteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class VenteVisitorTest {
  private static final LocalDate AJD = LocalDate.now();
  private static final LocalDate DATE_FUTURE = AJD.plusYears(1);
  private static final LocalDate DATE_PASSEE = AJD.minusYears(1);

  private VariableVisitor variableVisitor;
  private VenteVisitor subject;
  private UnitTestVisitor visitor;

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
  }

  @Test
  void parse_vente_materiel_standard() {
    Materiel materiel = new Materiel("voiture", AJD, AJD, ariary(10_000_000), 10.0);
    Compte compteBeneficiaire = new Compte("comptePrincipal", AJD, ariary(5_000_000));

    variableVisitor.addToScope("voiture", TRESORERIES, materiel);
    variableVisitor.addToScope("compte", COMPTES, compteBeneficiaire);
    variableVisitor.addToScope("ajd", DATE, AJD);

    var input =
        "* `vente` Dates:ajd, vendre Trésoreries:voiture pour 8000000Ar vers Comptes:compte";

    Vente actual = visitor.visit(input, PatriLangParser::vente);

    assertEquals(AJD, actual.getTVente());
    assertEquals(materiel, actual.getPossession());
    assertEquals(compteBeneficiaire, actual.getCompteBeneficiaire());
    assertEquals(ariary(8_000_000), actual.getPrixVente());
  }

  @Test
  void parse_vente_materiel_date_future() {
    Materiel materiel = new Materiel("voiture", AJD, AJD, ariary(10_000_000), 10.0);
    Compte compteBeneficiaire = new Compte("comptePrincipal", AJD, ariary(5_000_000));

    variableVisitor.addToScope("voiture", TRESORERIES, materiel);
    variableVisitor.addToScope("compte", COMPTES, compteBeneficiaire);
    variableVisitor.addToScope("dateFuture", DATE, DATE_FUTURE);

    var input =
        "* `vente` Dates:dateFuture, vendre Trésoreries:voiture pour 8000000Ar vers Comptes:compte";

    Vente actual = visitor.visit(input, PatriLangParser::vente);

    assertEquals(DATE_FUTURE, actual.getTVente());
    assertEquals(materiel, actual.getPossession());
    assertEquals(compteBeneficiaire, actual.getCompteBeneficiaire());
    assertEquals(ariary(8_000_000), actual.getPrixVente());
  }

  @Test
  void parse_vente_materiel_date_passee() {
    Materiel materiel = new Materiel("voiture", DATE_PASSEE, DATE_PASSEE, ariary(10_000_000), 10.0);
    Compte compteBeneficiaire = new Compte("comptePrincipal", AJD, ariary(5_000_000));

    variableVisitor.addToScope("voiture", TRESORERIES, materiel);
    variableVisitor.addToScope("compte", COMPTES, compteBeneficiaire);
    variableVisitor.addToScope("datePassee", DATE, DATE_PASSEE);

    var input =
        "* `vente` Dates:datePassee, vendre Trésoreries:voiture pour 8000000Ar vers Comptes:compte";

    Vente actual = visitor.visit(input, PatriLangParser::vente);

    assertEquals(DATE_PASSEE, actual.getTVente());
    assertEquals(materiel, actual.getPossession());
    assertEquals(compteBeneficiaire, actual.getCompteBeneficiaire());
    assertEquals(ariary(8_000_000), actual.getPrixVente());
  }

  @Test
  void parse_vente_avec_expression_complexe_prix() {
    Materiel materiel = new Materiel("voiture", AJD, AJD, ariary(10_000_000), 10.0);
    Compte compteBeneficiaire = new Compte("comptePrincipal", AJD, ariary(5_000_000));
    variableVisitor.addToScope("voiture", TRESORERIES, materiel);
    variableVisitor.addToScope("compte", COMPTES, compteBeneficiaire);
    variableVisitor.addToScope("ajd", DATE, AJD);
    variableVisitor.addToScope("prixBase", NOMBRE, 7_000_000d);
    variableVisitor.addToScope("rabais", NOMBRE, 1_000_000d);

    var input =
        "* `vente` Dates:ajd, vendre Trésoreries:voiture pour (Nombres:prixBase - Nombres:rabais)Ar"
            + " vers Comptes:compte";

    Vente actual = visitor.visit(input, PatriLangParser::vente);

    assertEquals(AJD, actual.getTVente());
    assertEquals(materiel, actual.getPossession());
    assertEquals(compteBeneficiaire, actual.getCompteBeneficiaire());
    assertEquals(ariary(6_000_000), actual.getPrixVente());
  }

  @Test
  void parse_vente_possession_inconnue() {
    Compte compte = new Compte("compte", AJD, ariary(0));

    variableVisitor.addToScope("compte", COMPTES, compte);
    variableVisitor.addToScope("ajd", DATE, AJD);

    var input =
        "* `vente` Dates:ajd, vendre Trésoreries:inexistante pour 1000Ar vers Comptes:compte";

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> visitor.visit(input, PatriLangParser::vente));

    assertTrue(exception.getMessage().contains("inexistante"));
  }

  @Test
  void parse_vente_compte_beneficiaire_inconnu() {
    Materiel materiel = new Materiel("objet", AJD, AJD, ariary(1000), 1.0);

    variableVisitor.addToScope("objet", TRESORERIES, materiel);
    variableVisitor.addToScope("ajd", DATE, AJD);

    var input = "* `vente` Dates:ajd, vendre Trésoreries:objet pour 1000Ar vers Comptes:inexistant";

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> visitor.visit(input, PatriLangParser::vente));

    assertTrue(exception.getMessage().contains("inexistant"));
  }

  @Test
  void parse_vente_date_invalide() {
    Materiel materiel = new Materiel("objet", AJD, AJD, ariary(1000), 1.0);
    Compte compte = new Compte("compte", AJD, ariary(0));

    variableVisitor.addToScope("objet", TRESORERIES, materiel);
    variableVisitor.addToScope("compte", COMPTES, compte);

    var input =
        "* `vente` Dates:inexistante, vendre Trésoreries:objet pour 1000Ar vers Comptes:compte";

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> visitor.visit(input, PatriLangParser::vente));

    assertTrue(exception.getMessage().contains("inexistante"));
  }
}
