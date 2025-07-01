package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.AcheterMaterielContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
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
        public AchatMaterielAuComptant visitAcheterMateriel(AcheterMaterielContext ctx) {
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
        "`achatOrdinateur` le 01 du 01-2025, acheter ordinateur, valant 300000Ar, s'appréciant"
            + " annuellement de 1%, depuis Trésoreries:monCompte";

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
}
