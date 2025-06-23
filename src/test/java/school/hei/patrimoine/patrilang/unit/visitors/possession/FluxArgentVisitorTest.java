package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static java.time.Month.JUNE;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentEntrerContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentSortirContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;
import static school.hei.patrimoine.patrilang.utils.Comparator.assertFluxArgentEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.ExpressionVisitor;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.ArgentVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.FluxArgentVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class FluxArgentVisitorTest {
  private static final VariableVisitor variableVisitor = new VariableVisitor();
  private static final LocalDate AJD = LocalDate.of(2025, JUNE, 23);
  private static final Compte COMPTE_PERSONNEL =
      new Compte("comptePersonnel", AJD, ariary(500_000));

  static {
    variableVisitor.addToScope("ajd", DATE, AJD);
    variableVisitor.addToScope("comptePersonnel", TRESORERIES, COMPTE_PERSONNEL);
  }

  FluxArgentVisitor subject =
      new FluxArgentVisitor(
          variableVisitor,
          new ArgentVisitor(new ExpressionVisitor()),
          new IdVisitor(variableVisitor));

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
  void parse_sortir_flux_argent_without_date_fin() {
    var input =
        """
    * `fluxArgentSortir + Dates:ajd` Dates:ajd, sortir 500000Ar depuis Trésoreries:comptePersonnel
""";
    var expected =
        new FluxArgent("fluxArgentSortir" + AJD, COMPTE_PERSONNEL, AJD, ariary(-500_000));

    FluxArgent actual = visitor.visit(input, PatriLangParser::fluxArgentSortir);

    assertFluxArgentEquals(expected, actual);
  }

  @Test
  void parse_entrer_flux_argent_with_date_fin() {
    var input =
        """
    * `fluxArgentEntrer + Dates:ajd` Dates:ajd, entrer 500000Ar vers Trésoreries:comptePersonnel, jusqu'à DATE_MAX tous les 2 du mois
""";
    var expected =
        new FluxArgent(
            "fluxArgentEntrer" + AJD, COMPTE_PERSONNEL, AJD, LocalDate.MAX, 2, ariary(500_000));

    FluxArgent actual = visitor.visit(input, PatriLangParser::fluxArgentEntrer);

    assertFluxArgentEquals(expected, actual);
  }

  @Test
  void parse_sortir_flux_argent_with_date_fin() {
    var input =
        """
    * `fluxArgentSortir + Dates:ajd` Dates:ajd, sortir 500000Ar depuis Trésoreries:comptePersonnel, jusqu'à DATE_MAX tous les 2 du mois
""";
    var expected =
        new FluxArgent(
            "fluxArgentSortir" + AJD, COMPTE_PERSONNEL, AJD, LocalDate.MAX, 2, ariary(-500_000));

    FluxArgent actual = visitor.visit(input, PatriLangParser::fluxArgentSortir);

    assertFluxArgentEquals(expected, actual);
  }
}
