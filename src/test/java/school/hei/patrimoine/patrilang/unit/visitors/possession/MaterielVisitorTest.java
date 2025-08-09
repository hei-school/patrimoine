package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.PossedeMaterielContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VenteContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Vente;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.MaterielVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.VenteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class MaterielVisitorTest {
  private static final LocalDate AJD = LocalDate.of(2025, 6, 23);
  private static final Compte compteBeneficiaire = new Compte("compteBeneficiaire", AJD, ariary(0));
  private final VariableVisitor variableVisitor = new VariableVisitor();

  MaterielVisitor subject = new MaterielVisitor(variableVisitor);

  VenteVisitor venteVisitor = new VenteVisitor(variableVisitor);

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public Materiel visitPossedeMateriel(PossedeMaterielContext ctx) {
          return subject.apply(ctx);
        }
      };

  UnitTestVisitor venteTestVisitor =
      new UnitTestVisitor() {
        @Override
        public Vente visitVente(VenteContext ctx) {
          return venteVisitor.apply(ctx);
        }
      };

  @BeforeEach
  void setUp() {
    variableVisitor.addToScope("ajd", DATE, AJD);
    variableVisitor.addToScope("compteBeneficiaire", TRESORERIES, compteBeneficiaire);
    subject = new MaterielVisitor(variableVisitor);
  }

  @Test
  void parser_materiel_who_has_appreciation_positive() {
    var au1Janvier2026 = LocalDate.of(2026, 1, 1);
    var input =
        """
    * `possèdeMateriel`, Dates:ajd posséder ordinateur valant 200000Ar, s'appréciant annuellement de 1%
""";

    var expected = new Materiel("ordinateur", AJD, AJD, ariary(200_000), 0.01);

    Materiel actual = visitor.visit(input, PatriLangParser::possedeMateriel);

    assertEquals(expected.nom(), actual.nom());
    assertEquals(expected.valeurComptable(), actual.valeurComptable());
    assertEquals(
        expected.projectionFuture(au1Janvier2026).valeurComptable(),
        actual.projectionFuture(au1Janvier2026).valeurComptable());
  }

  @Test
  void parser_materiel_who_has_appreciation_negative() {
    var au1Janvier2026 = LocalDate.of(2026, 1, 1);
    var input =
        """
    * `possèdeMateriel`, Dates:ajd posséder ordinateur valant 200000Ar, se dépréciant annuellement de 20%
""";

    var expected = new Materiel("ordinateur", AJD, AJD, ariary(200_000), -0.2);

    Materiel actual = visitor.visit(input, PatriLangParser::possedeMateriel);

    assertEquals(expected.nom(), actual.nom());
    assertEquals(expected.valeurComptable(), actual.valeurComptable());
    assertEquals(
        expected.projectionFuture(au1Janvier2026).valeurComptable(),
        actual.projectionFuture(au1Janvier2026).valeurComptable());
  }

  @Test
  void should_handle_materiel_creation_and_sale_in_same_scope() {
    var inputCreation =
        """
* `possèdeMateriel`, Dates:ajd posséder ordinateur valant 200000Ar, s'appréciant annuellement de 1%
""";

    var inputVente =
        """
* `vente` Dates:ajd, vendre Matériel:ordinateur pour 150000Ar vers Trésoreries:compteBeneficiaire
""";

    Materiel expected = visitor.visit(inputCreation, PatriLangParser::possedeMateriel);
    Vente actual = venteTestVisitor.visit(inputVente, PatriLangParser::vente);

    assertEquals(expected, actual.getPossession());
    assertEquals(expected.nom(), actual.getPossession().nom());
    assertEquals(compteBeneficiaire, actual.getCompteBeneficiaire());
    assertEquals(ariary(150_000), actual.getPrixVente());
    assertTrue(expected.estVendu(AJD));

    var actualVariable = variableVisitor.getVariableScope().get("ordinateur", MATERIEL);
    assertEquals(expected, actualVariable.value());
  }

  @Test
  void should_share_materiel_between_different_scopes() {
    var parentScope = new VariableScope();
    parentScope.add("ajd", DATE, AJD);
    parentScope.add("compteBeneficiaire", TRESORERIES, compteBeneficiaire);

    VariableVisitor declarationVisitor = new VariableVisitor(Optional.of(parentScope));
    MaterielVisitor materielVisitor = new MaterielVisitor(declarationVisitor);
    VariableVisitor venteVisitor = new VariableVisitor(Optional.of(parentScope));
    VenteVisitor venteVisitorImpl = new VenteVisitor(venteVisitor);

    var inputDeclare =
        """
* `possèdeMateriel`, Dates:ajd posséder télévision valant 300000Ar, s'appréciant annuellement de 5%
""";

    var inputVente =
        """
* `vente` Dates:ajd, vendre Matériel:télévision pour 350000Ar vers Trésoreries:compteBeneficiaire
""";

    Materiel expected =
        new UnitTestVisitor() {
          @Override
          public Materiel visitPossedeMateriel(PossedeMaterielContext ctx) {
            return materielVisitor.apply(ctx);
          }
        }.visit(inputDeclare, PatriLangParser::possedeMateriel);

    Vente actual =
        new UnitTestVisitor() {
          @Override
          public Vente visitVente(VenteContext ctx) {
            return venteVisitorImpl.apply(ctx);
          }
        }.visit(inputVente, PatriLangParser::vente);

    assertAll(
        () -> assertEquals(expected, actual.getPossession()),
        () -> assertEquals(ariary(350_000), actual.getPrixVente()),
        () -> assertTrue(expected.estVendu(AJD)));
  }

  @Test
  void should_propagate_variables_to_child_scope() {
    var parentScope = new VariableScope();
    parentScope.add(
        "global", MATERIEL, new Materiel("bicyclette", AJD, AJD, ariary(300_000), -1.1));
    var childScope = new VariableScope(Optional.of(parentScope));

    var actual = childScope.get("global", MATERIEL).value();

    assertNotNull(actual);
  }

  @Test
  void should_not_leak_child_variables_to_parent() {
    var parentScope = new VariableScope();
    var childScope = new VariableScope(Optional.of(parentScope));
    childScope.add("local", MATERIEL, new Materiel("ardoise", AJD, AJD, ariary(4_000), -0.5));

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          parentScope.get("local", MATERIEL);
        });
  }
}
