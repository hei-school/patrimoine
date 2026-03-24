package school.hei.patrimoine.patrilang.unit.visitors.possession.vente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import school.hei.patrimoine.patrilang.visitors.possession.vente.VenteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class VenteVisitorTest {
  private static final LocalDate T0 = LocalDate.of(2025, 1, 1);
  private static final LocalDate T_VENTE = LocalDate.of(2025, 6, 1);

  private Materiel voiture;
  private Compte compte;
  private VenteVisitor subject;
  private UnitTestVisitor visitor;

  @BeforeEach
  void setUp() {
    VariableVisitor variableVisitor = new VariableVisitor();
    voiture = new Materiel("voiture", T0, T0, ariary(10_000_000), -0.1);
    compte = new Compte("compteCourant", T0, ariary(0));

    variableVisitor.addToScope("t0", DATE, T0);
    variableVisitor.addToScope("tVente", DATE, T_VENTE);
    variableVisitor.addToScope("voiture", MATERIEL, voiture);
    variableVisitor.addToScope("compteCourant", TRESORERIES, compte);

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
  void parse_vente_avec_date_variable() {
    var input =
        "* `venteVoiture`, Dates:tVente vendre Matériel:voiture pour 9000000Ar vers"
            + " Trésoreries:compteCourant";

    Vente actual = visitor.visit(input, PatriLangParser::vente);

    assertEquals(T_VENTE, actual.getTVente());
    assertEquals(voiture, actual.getPossession());
    assertEquals(compte, actual.getCompteBeneficiaire());
    assertEquals(ariary(9_000_000), actual.getPrixVente());
  }

  @Test
  void parse_vente_marque_possession_comme_vendue() {
    var input =
        "* `venteVoiture`, Dates:tVente vendre Matériel:voiture pour 9000000Ar vers"
            + " Trésoreries:compteCourant";

    visitor.visit(input, PatriLangParser::vente);

    assertTrue(voiture.estVendu(T_VENTE));
    assertTrue(voiture.estVendu(T_VENTE.plusDays(1)));
  }

  @Test
  void parse_vente_avec_date_litterale() {
    var input =
        "* `venteVoiture2`, le 01 juin 2025 vendre Matériel:voiture pour 8000000Ar vers"
            + " Trésoreries:compteCourant";

    Vente actual = visitor.visit(input, PatriLangParser::vente);

    assertEquals(T_VENTE, actual.getTVente());
    assertEquals(ariary(8_000_000), actual.getPrixVente());
  }

  @Test
  void parse_vente_credite_le_compte_beneficiaire() {
    var input =
        "* `venteVoiture`, Dates:tVente vendre Matériel:voiture pour 9000000Ar vers"
            + " Trésoreries:compteCourant";

    visitor.visit(input, PatriLangParser::vente);

    var soldeFutur = compte.projectionFuture(T_VENTE.plusMonths(1)).valeurComptable();
    assertEquals(ariary(9_000_000), soldeFutur);
  }
}
