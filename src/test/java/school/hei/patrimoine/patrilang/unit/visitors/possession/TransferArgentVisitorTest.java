package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.FluxArgentTransfererContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;
import static school.hei.patrimoine.patrilang.utils.Comparator.assertFluxArgentEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.ArgentVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.TransferArgentVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class TransferArgentVisitorTest {
  private static final VariableVisitor variableVisitor = new VariableVisitor();
  private static final LocalDate AJD = LocalDate.of(2025, 6, 23);
  private static final Compte COMPTE_DÉBITEUR = new Compte("débiteur", AJD, ariary(600_000));
  private static final Compte COMPTE_CRÉDITEUR = new Compte("créditeur", AJD, ariary(1_600_000));

  TransferArgentVisitor subject =
      new TransferArgentVisitor(
          variableVisitor, new ArgentVisitor(variableVisitor), new IdVisitor(variableVisitor));

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public TransfertArgent visitFluxArgentTransferer(FluxArgentTransfererContext ctx) {
          return subject.apply(ctx);
        }
      };

  static {
    variableVisitor.addToScope("ajd", DATE, AJD);
    variableVisitor.addToScope("créditeur", TRESORERIES, COMPTE_CRÉDITEUR);
    variableVisitor.addToScope("débiteur", TRESORERIES, COMPTE_DÉBITEUR);
  }

  @Test
  void parse_transfert_without_date_fin() {
    var fluxEntrerName = "Flux TransfertArgent entrant: transférerArgent";
    var fluxSortirName = "Flux TransfertArgent sortant: transférerArgent";

    var input =
        """
    * `transférerArgent`, Dates:ajd transférer 200000Ar depuis Trésoreries:créditeur vers Trésoreries:débiteur
""";

    visitor.visit(input, PatriLangParser::fluxArgentTransferer);

    var expectedFluxEntrer = new FluxArgent(fluxEntrerName, COMPTE_DÉBITEUR, AJD, ariary(200_000));
    var expectedFluxSortir =
        new FluxArgent(fluxSortirName, COMPTE_CRÉDITEUR, AJD, ariary(-200_000));

    var actualFluxEntrer =
        COMPTE_DÉBITEUR.getFluxArgents().stream()
            .filter(flux -> flux.nom().equals(fluxEntrerName))
            .findFirst()
            .orElseThrow();
    var actualFluxSortir =
        COMPTE_CRÉDITEUR.getFluxArgents().stream()
            .filter(flux -> flux.nom().equals(fluxSortirName))
            .findFirst()
            .orElseThrow();

    assertFluxArgentEquals(expectedFluxEntrer, actualFluxEntrer);
    assertFluxArgentEquals(expectedFluxSortir, actualFluxSortir);
  }

  @Test
  void parse_transfert_with_date_fin() {
    var fluxEntrerName = "Flux TransfertArgent entrant: transférerArgentWithDateFin";
    var fluxSortirName = "Flux TransfertArgent sortant: transférerArgentWithDateFin";

    var input =
        """
    * `transférerArgentWithDateFin`, Dates:ajd transférer 200000Ar depuis Trésoreries:créditeur vers Trésoreries:débiteur, jusqu'à DATE_MAX tous les 1 du mois
""";

    visitor.visit(input, PatriLangParser::fluxArgentTransferer);

    var expectedFluxEntrer =
        new FluxArgent(fluxEntrerName, COMPTE_DÉBITEUR, AJD, LocalDate.MAX, 1, ariary(200_000));
    var expectedFluxSortir =
        new FluxArgent(fluxSortirName, COMPTE_CRÉDITEUR, AJD, LocalDate.MAX, 1, ariary(-200_000));

    var actualFluxEntrer =
        COMPTE_DÉBITEUR.getFluxArgents().stream()
            .filter(flux -> flux.nom().equals(fluxEntrerName))
            .findFirst()
            .orElseThrow();
    var actualFluxSortir =
        COMPTE_CRÉDITEUR.getFluxArgents().stream()
            .filter(flux -> flux.nom().equals(fluxSortirName))
            .findFirst()
            .orElseThrow();

    assertFluxArgentEquals(expectedFluxEntrer, actualFluxEntrer);
    assertFluxArgentEquals(expectedFluxSortir, actualFluxSortir);
  }
}
