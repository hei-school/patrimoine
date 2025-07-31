package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Vente;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.VenteContext;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.VenteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class VenteVisitorTest {
  private static final LocalDate AJD = LocalDate.now();
  private static final LocalDate DATE_FUTURE = AJD.plusYears(1);
  private static final LocalDate DATE_PASSEE = AJD.minusYears(1);
  private static final Materiel MATERIEL1 =
      new Materiel("Voiture", AJD, AJD, ariary(10_000_000), 10.0);
  private static final Materiel MATERIEL2 =
      new Materiel("Moto", AJD, AJD, ariary(10_000_000), 10.0);
  private static final Materiel MATERIEL3 =
      new Materiel("Radio", AJD, AJD, ariary(10_000_000), 10.0);
  private static final Compte COMPTE_BENEFICIAIRE =
      new Compte("comptePrincipal", AJD, ariary(5_000_000));

  private static final VariableVisitor variableVisitor = new VariableVisitor();

  VenteVisitor subject = new VenteVisitor(variableVisitor);

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public Vente visitVente(VenteContext ctx) {
          return subject.apply(ctx);
        }
      };

  static {
    variableVisitor.addToScope("ajd", DATE, AJD);
    variableVisitor.addToScope("datePassee", DATE, DATE_PASSEE);
    variableVisitor.addToScope("dateFuture", DATE, DATE_FUTURE);
    variableVisitor.addToScope("voiture", MATERIEL, MATERIEL1);
    variableVisitor.addToScope("moto", MATERIEL, MATERIEL2);
    variableVisitor.addToScope("radio", MATERIEL, MATERIEL3);
    variableVisitor.addToScope("compteBeneficiaire", TRESORERIES, COMPTE_BENEFICIAIRE);
    variableVisitor.addToScope("prixBase", NOMBRE, 7_000_000d);
    variableVisitor.addToScope("rabais", NOMBRE, 1_000_000d);
  }

  @Test
  void parse_vente_materiel_standard() {
    var input =
        """
* `vente` Dates:ajd, vendre Matériel:moto pour 8000000Ar vers Trésoreries:compteBeneficiaire
""";

    var possession = new Materiel("Moto", AJD, AJD, ariary(10_000_000), 10.0);
    var expected = new Vente(AJD, possession, COMPTE_BENEFICIAIRE, ariary(8_000_000));

    Vente actual = visitor.visit(input, PatriLangParser::vente);

    assertEquals(expected.getTVente(), actual.getTVente());
    assertEquals(expected.getPossession(), actual.getPossession());
    assertEquals(expected.getCompteBeneficiaire(), actual.getCompteBeneficiaire());
    assertEquals(expected.getPrixVente(), actual.getPrixVente());
  }

  @Test
  void parse_vente_materiel_date_future() {
    var input =
        """
* `vente` Dates:dateFuture, vendre Matériel:voiture pour 8000000Ar vers Trésoreries:compteBeneficiaire
""";

    var possession = new Materiel("Voiture", AJD, AJD, ariary(10_000_000), 10.0);
    var expected = new Vente(DATE_FUTURE, possession, COMPTE_BENEFICIAIRE, ariary(8_000_000));

    Vente actual = visitor.visit(input, PatriLangParser::vente);

    assertEquals(expected.getTVente(), actual.getTVente());
    assertEquals(expected.getPossession(), actual.getPossession());
    assertEquals(expected.getCompteBeneficiaire(), actual.getCompteBeneficiaire());
    assertEquals(expected.getPrixVente(), actual.getPrixVente());
  }

  @Test
  void parse_vente_materiel_date_passee() {
    var input =
        """
* `vente` Dates:datePassee, vendre Matériel:voiture pour 8000000Ar vers Trésoreries:compteBeneficiaire
""";

    var possession = new Materiel("Voiture", AJD, AJD, ariary(10_000_000), 10.0);
    var expected = new Vente(DATE_PASSEE, possession, COMPTE_BENEFICIAIRE, ariary(8_000_000));

    Vente actual = visitor.visit(input, PatriLangParser::vente);

    assertEquals(expected.getTVente(), actual.getTVente());
    assertEquals(expected.getPossession(), actual.getPossession());
    assertEquals(expected.getCompteBeneficiaire(), actual.getCompteBeneficiaire());
    assertEquals(expected.getPrixVente(), actual.getPrixVente());
  }

  @Test
  void parse_vente_avec_expression_complexe_prix() {
    var input =
        """
* `vente` Dates:ajd, vendre Matériel:radio pour (Nombres:prixBase - Nombres:rabais)Ar vers Trésoreries:compteBeneficiaire
""";

    var possession = new Materiel("Radio", AJD, AJD, ariary(10_000_000), 10.0);
    var expected = new Vente(AJD, possession, COMPTE_BENEFICIAIRE, ariary(6_000_000));

    Vente actual = visitor.visit(input, PatriLangParser::vente);

    assertEquals(expected.getTVente(), actual.getTVente());
    assertEquals(expected.getPossession(), actual.getPossession());
    assertEquals(expected.getCompteBeneficiaire(), actual.getCompteBeneficiaire());
    assertEquals(expected.getPrixVente(), actual.getPrixVente());
  }

  @Test
  void parse_vente_possession_inconnue() {
    var input =
        """
* `vente` Dates:ajd, vendre Matériel:inexistante pour 1000Ar vers Trésoreries:compteBeneficiaire
""";

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> visitor.visit(input, PatriLangParser::vente));

    assertTrue(exception.getMessage().contains("inexistante"));
  }

  @Test
  void parse_vente_compteBeneficiaire_beneficiaire_inconnu() {
    var input =
        """
        * `vente` Dates:ajd, vendre Matériel:voiture pour 1000Ar vers Trésoreries:inexistant
        """;

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> visitor.visit(input, PatriLangParser::vente));

    assertTrue(exception.getMessage().contains("inexistant"));
  }

  @Test
  void parse_vente_date_invalide() {
    var input =
        """
* `vente` Dates:inexistante, vendre Matériel:voiture pour 1000Ar vers Trésoreries:compteBeneficiaire
""";

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> visitor.visit(input, PatriLangParser::vente));

    assertTrue(exception.getMessage().contains("inexistante"));
  }
}
