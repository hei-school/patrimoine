package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.AcheterMaterielContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.AchatMaterielVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class AchatMaterielVisitorTest {
  private static final VariableVisitor variableVisitor = new VariableVisitor();
  private static final Compte MON_COMPTE = new Compte("monCompte", LocalDate.MIN, ariary(200_000));
  AchatMaterielVisitor subject = new AchatMaterielVisitor(variableVisitor);

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public OperationComptable visitAcheterMateriel(AcheterMaterielContext ctx) {
          return subject.apply(ctx);
        }
      };

  static {
    variableVisitor.addToScope("monCompte", TRESORERIES, MON_COMPTE);
  }

  @Test
  void parse_normal_achat_materiel() {
    var expectedFluxArgentName = "Financement AchatMaterielAuComptant: ordinateur";
    var input =
        "`achatOrdinateur` le 01 du 01-2025, acheter ordinateur, valant 300000Ar, depuis"
            + " Trésoreries:monCompte, s'appréciant annuellement de 1%";

    visitor.visit(input, PatriLangParser::acheterMateriel);

    var actualFluxArgent =
        MON_COMPTE.getFluxArgents().stream()
            .filter(fluxArgent -> fluxArgent.nom().equals(expectedFluxArgentName))
            .findFirst()
            .orElseThrow();

    assertEquals(ariary(-300_000), actualFluxArgent.getFluxMensuel());
    assertEquals(MON_COMPTE, actualFluxArgent.getCompte());
    assertEquals(LocalDate.of(2025, JANUARY, 1), actualFluxArgent.getDebut());
  }

  @Test
  void acheter_materiel_has_default_type_immobilisation() {
    var input =
        """
    * `achatMateriel` le 5 du 2-2026, acheter Voiture valant 20_000_000Ar depuis Trésoreries:monCompte s'appréciant annuellement de 0%
""";

    OperationComptable actual = visitor.visit(input, PatriLangParser::acheterMateriel);

    assertInstanceOf(AchatMaterielAuComptant.class, actual.possession());
    assertEquals(IMMOBILISATION, actual.type());
  }

  @Test
  void acheter_materiel_reject_defined_type() {
    var input =
        """
    * `CHG achatMateriel` le 10 du 1-2026, acheter Voiture valant 20_000_000Ar depuis Trésoreries:monCompte s'appréciant annuellement de 0%
""";

    OperationComptable actual = visitor.visit(input, PatriLangParser::acheterMateriel);

    assertEquals(IMMOBILISATION, actual.type());
  }
}
