package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static java.time.Month.JUNE;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentEntrerContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentSortirContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;
import static school.hei.patrimoine.patrilang.utils.Comparator.assertFluxArgentEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.possession.*;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.FluxArgentVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class FluxVariableArgentVisitorTest {
  private static final VariableVisitor variableVisitor = new VariableVisitor();
  private static final LocalDate AJD = LocalDate.of(2025, JUNE, 23);
  private static final Compte COMPTE_PERSONNEL =
      new Compte("comptePersonnel", AJD, ariary(500_000));

  static {
    variableVisitor.addToScope("ajd", DATE, AJD);
    variableVisitor.addToScope("comptePersonnel", TRESORERIES, COMPTE_PERSONNEL);
  }

  FluxArgentVisitor subject =
      new FluxArgentVisitor(variableVisitor, new IdVisitor(variableVisitor));

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public OperationComptable visitFluxArgentEntrer(FluxArgentEntrerContext ctx) {
          return subject.apply(ctx);
        }

        @Override
        public OperationComptable visitFluxArgentSortir(FluxArgentSortirContext ctx) {
          return subject.apply(ctx);
        }
      };

  @Test
  void parse_entrer_flux_argent_without_date_fin() {
    var input =
        """
            * `fluxArgentEntrer` Dates:ajd, entrer 500000Ar vers Trésoreries:comptePersonnel
        """;
    var expected = new FluxArgent("fluxArgentEntrer", COMPTE_PERSONNEL, AJD, ariary(500_000));

    OperationComptable actual = visitor.visit(input, PatriLangParser::fluxArgentEntrer);

    assertFluxArgentEquals(expected, (FluxArgent) actual.possession());
  }

  @Test
  void parse_sortir_flux_argent_without_date_fin() {
    var formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    var input =
        """
    * `fluxArgentSortir + Dates:ajd` Dates:ajd, sortir 500000Ar depuis Trésoreries:comptePersonnel
""";
    var expected =
        new FluxArgent(
            "fluxArgentSortir" + AJD.format(formatter), COMPTE_PERSONNEL, AJD, ariary(-500_000));

    OperationComptable actual = visitor.visit(input, PatriLangParser::fluxArgentSortir);

    assertFluxArgentEquals(expected, (FluxArgent) actual.possession());
  }

  @Test
  void parse_entrer_flux_argent_with_date_fin() {
    var formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    var input =
        """
    * `fluxArgentEntrer + Dates:ajd` Dates:ajd, entrer 500000Ar vers Trésoreries:comptePersonnel, jusqu'à DATE_MAX tous les 2 du mois
""";
    var expected =
        new FluxArgent(
            "fluxArgentEntrer" + AJD.format(formatter),
            COMPTE_PERSONNEL,
            AJD,
            LocalDate.MAX,
            2,
            ariary(500_000));

    OperationComptable actual = visitor.visit(input, PatriLangParser::fluxArgentEntrer);

    assertFluxArgentEquals(expected, (FluxArgent) actual.possession());
  }

  @Test
  void parse_sortir_flux_argent_with_date_fin() {
    var formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    var input =
        """
    * `fluxArgentSortir + Dates:ajd` Dates:ajd, sortir 500000Ar depuis Trésoreries:comptePersonnel, jusqu'à DATE_MAX tous les 2 du mois
""";
    var expected =
        new FluxArgent(
            "fluxArgentSortir" + AJD.format(formatter),
            COMPTE_PERSONNEL,
            AJD,
            LocalDate.MAX,
            2,
            ariary(-500_000));

    OperationComptable actual = visitor.visit(input, PatriLangParser::fluxArgentSortir);

    assertFluxArgentEquals(expected, (FluxArgent) actual.possession());
  }

  @Test
  void parse_sortir_flux_argent_with_date_fin_du_mois() {
    var formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    var input =
        """
    * `fluxArgentSortir + Dates:ajd` Dates:ajd, sortir 500000Ar depuis Trésoreries:comptePersonnel, jusqu'à DATE_MAX tous les fin du mois
""";
    var expected =
        new FluxArgent(
            "fluxArgentSortir" + AJD.format(formatter),
            COMPTE_PERSONNEL,
            AJD,
            LocalDate.MAX,
            31,
            ariary(-500_000));

    OperationComptable actual = visitor.visit(input, PatriLangParser::fluxArgentSortir);

    assertFluxArgentEquals(expected, (FluxArgent) actual.possession());
  }

  @Test
  void parse_entrer_flux_argent_without_date_fin_with_type_fec() {
    var input1 =
        """
            * `PRD fluxArgentEntrer` Dates:ajd, entrer 500000Ar vers Trésoreries:comptePersonnel
        """;

    var input2 =
        """
            * `AUTRE fluxArgentEntrer` Dates:ajd, entrer 500000Ar vers Trésoreries:comptePersonnel
        """;

    var input3 =
        """
            * `IMMO fluxArgentEntrer` Dates:ajd, entrer 500000Ar vers Trésoreries:comptePersonnel
        """;

    OperationComptable actual1 = visitor.visit(input1, PatriLangParser::fluxArgentEntrer);
    OperationComptable actual2 = visitor.visit(input2, PatriLangParser::fluxArgentEntrer);
    OperationComptable actual3 = visitor.visit(input3, PatriLangParser::fluxArgentEntrer);

    assertEquals(PRODUIT, actual1.type());
    assertEquals(AUTRE, actual2.type());
    assertEquals(IMMOBILISATION, actual3.type());
  }

  @Test
  void parse_sortir_flux_argent_avec_date_fin_du_mois_type_fec() {
    var input1 =
        """
    * `CCA fluxArgentSortir + Dates:ajd` Dates:ajd, sortir 500000Ar depuis Trésoreries:comptePersonnel, jusqu'à DATE_MAX tous les fin du mois
""";

    var input2 =
        """
    * `AUTRE fluxArgentSortir + Dates:ajd` Dates:ajd, sortir 500000Ar depuis Trésoreries:comptePersonnel jusqu'à DATE_MAX tous les fin du mois
""";

    var input3 =
        """
    * `CHG fluxArgentSortir + Dates:ajd` Dates:ajd, sortir 500000Ar depuis Trésoreries:comptePersonnel, jusqu'à DATE_MAX tous les fin du mois
""";

    OperationComptable actual1 = visitor.visit(input1, PatriLangParser::fluxArgentSortir);
    OperationComptable actual2 = visitor.visit(input2, PatriLangParser::fluxArgentSortir);
    OperationComptable actual3 = visitor.visit(input3, PatriLangParser::fluxArgentSortir);

    assertEquals(CCA, actual1.type());
    assertEquals(AUTRE, actual2.type());
    assertEquals(CHARGE, actual3.type());
  }

  @Test
  void parse_entrer_flux_argent_avec_date_fin_du_mois_type_fec() {
    var input1 =
        """
    * `IMMO fluxArgentEntrer + Dates:ajd` Dates:ajd, entrer 500000Ar vers Trésoreries:comptePersonnel, jusqu'à DATE_MAX tous les fin du mois
""";

    var input2 =
        """
    * `PRD fluxArgentEntrer + Dates:ajd` Dates:ajd, entrer 500000Ar vers Trésoreries:comptePersonnel, jusqu'à DATE_MAX tous les fin du mois
""";

    var input3 =
        """
    * `CCA fluxArgentEntrer + Dates:ajd` Dates:ajd, entrer 500000Ar vers Trésoreries:comptePersonnel, jusqu'à DATE_MAX tous les fin du mois
""";

    var input4 =
        """
    * `CHG fluxArgentEntrer + Dates:ajd` Dates:ajd, entrer 500000Ar vers Trésoreries:comptePersonnel, jusqu'à DATE_MAX tous les fin du mois
""";

    OperationComptable actual1 = visitor.visit(input1, PatriLangParser::fluxArgentEntrer);
    OperationComptable actual2 = visitor.visit(input2, PatriLangParser::fluxArgentEntrer);
    OperationComptable actual3 = visitor.visit(input3, PatriLangParser::fluxArgentEntrer);
    OperationComptable actual4 = visitor.visit(input4, PatriLangParser::fluxArgentEntrer);

    assertEquals(IMMOBILISATION, actual1.type());
    assertEquals(PRODUIT, actual2.type());
    assertEquals(CCA, actual3.type());
    assertEquals(CHARGE, actual4.type());
  }
}
