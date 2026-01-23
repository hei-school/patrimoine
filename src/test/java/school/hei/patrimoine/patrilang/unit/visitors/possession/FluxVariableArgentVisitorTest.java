package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static java.time.Month.JUNE;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.possession.TypeFEC.*;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentEntrerContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentSortirContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;
import static school.hei.patrimoine.patrilang.utils.Comparator.assertFluxArgentEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
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
        public FluxArgent visitFluxArgentEntrer(FluxArgentEntrerContext ctx) {
          return subject.apply(ctx);
        }

        @Override
        public FluxArgent visitFluxArgentSortir(FluxArgentSortirContext ctx) {
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

    FluxArgent actual = visitor.visit(input, PatriLangParser::fluxArgentEntrer);

    assertFluxArgentEquals(expected, actual);
  }

  @Test
  void parse_entrer_flux_argent_without_date_fin_with_type_fec() {
    var input1 =
        """
    * `PRODUIT_fluxArgentEntrer` Dates:ajd, entrer 500000Ar vers Trésoreries:comptePersonnel
""";

    var input2 =
        """
            * `AUTRE_fluxArgentEntrer` Dates:ajd, entrer 500000Ar vers Trésoreries:comptePersonnel
        """;

    var input3 =
        """
    * `IMMOBILISATION_fluxArgentEntrer` Dates:ajd, entrer 500000Ar vers Trésoreries:comptePersonnel
""";
    var expected1 =
        new FluxArgent("PRODUIT_fluxArgentEntrer", COMPTE_PERSONNEL, AJD, ariary(500_000), PRODUIT);
    var expected2 =
        new FluxArgent("AUTRE_fluxArgentEntrer", COMPTE_PERSONNEL, AJD, ariary(500_000), AUTRE);
    var expected3 =
        new FluxArgent(
            "IMMOBILISATION_fluxArgentEntrer",
            COMPTE_PERSONNEL,
            AJD,
            ariary(500_000),
            IMMOBILISATION);

    FluxArgent actual1 = visitor.visit(input1, PatriLangParser::fluxArgentEntrer);
    FluxArgent actual2 = visitor.visit(input2, PatriLangParser::fluxArgentEntrer);
    FluxArgent actual3 = visitor.visit(input3, PatriLangParser::fluxArgentEntrer);

    assertFluxArgentEquals(expected1, actual1);
    assertFluxArgentEquals(expected2, actual2);
    assertFluxArgentEquals(expected3, actual3);
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

    FluxArgent actual = visitor.visit(input, PatriLangParser::fluxArgentSortir);

    assertFluxArgentEquals(expected, actual);
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

    FluxArgent actual = visitor.visit(input, PatriLangParser::fluxArgentEntrer);

    assertFluxArgentEquals(expected, actual);
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

    FluxArgent actual = visitor.visit(input, PatriLangParser::fluxArgentSortir);

    assertFluxArgentEquals(expected, actual);
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

    FluxArgent actual = visitor.visit(input, PatriLangParser::fluxArgentSortir);

    assertFluxArgentEquals(expected, actual);
  }

  @Test
  void parse_sortir_flux_argent_with_date_fin_du_mois_avec_type_fec() {
    var formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");

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

    var expected1 =
        new FluxArgent(
            "fluxArgentSortir" + AJD.format(formatter),
            COMPTE_PERSONNEL,
            AJD,
            LocalDate.MAX,
            31,
            ariary(-500_000),
            CCA);

    var expected2 =
        new FluxArgent(
            "fluxArgentSortir" + AJD.format(formatter),
            COMPTE_PERSONNEL,
            AJD,
            LocalDate.MAX,
            31,
            ariary(-500_000),
            AUTRE);

    var expected3 =
        new FluxArgent(
            "fluxArgentSortir" + AJD.format(formatter),
            COMPTE_PERSONNEL,
            AJD,
            LocalDate.MAX,
            31,
            ariary(-500_000),
            CHARGE);

    FluxArgent actual1 = visitor.visit(input1, PatriLangParser::fluxArgentSortir);
    FluxArgent actual2 = visitor.visit(input2, PatriLangParser::fluxArgentSortir);
    FluxArgent actual3 = visitor.visit(input3, PatriLangParser::fluxArgentSortir);

    assertFluxArgentEquals(expected1, actual1);
    assertFluxArgentEquals(expected2, actual2);
    assertFluxArgentEquals(expected3, actual3);
  }

  @Test
  void parse_entrer_flux_argent_with_date_fin_du_mois_avec_type_fec() {
    var formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
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

    var expected1 =
        new FluxArgent(
            "fluxArgentEntrer" + AJD.format(formatter),
            COMPTE_PERSONNEL,
            AJD,
            LocalDate.MAX,
            31,
            ariary(500_000),
            IMMOBILISATION);

    var expected2 =
        new FluxArgent(
            "fluxArgentEntrer" + AJD.format(formatter),
            COMPTE_PERSONNEL,
            AJD,
            LocalDate.MAX,
            31,
            ariary(500_000),
            PRODUIT);

    var expected3 =
        new FluxArgent(
            "fluxArgentEntrer" + AJD.format(formatter),
            COMPTE_PERSONNEL,
            AJD,
            LocalDate.MAX,
            31,
            ariary(500_000),
            CCA);

    var expected4 =
        new FluxArgent(
            "fluxArgentEntrer" + AJD.format(formatter),
            COMPTE_PERSONNEL,
            AJD,
            LocalDate.MAX,
            31,
            ariary(500_000),
            CHARGE);

    FluxArgent actual1 = visitor.visit(input1, PatriLangParser::fluxArgentEntrer);
    FluxArgent actual2 = visitor.visit(input2, PatriLangParser::fluxArgentEntrer);
    FluxArgent actual3 = visitor.visit(input3, PatriLangParser::fluxArgentEntrer);
    FluxArgent actual4 = visitor.visit(input4, PatriLangParser::fluxArgentEntrer);

    assertFluxArgentEquals(expected1, actual1);
    assertFluxArgentEquals(expected2, actual2);
    assertFluxArgentEquals(expected3, actual3);
    assertFluxArgentEquals(expected4, actual4);
  }
}
