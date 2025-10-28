package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static java.time.Month.JUNE;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.SousTitreContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.GroupPossessionVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class GroupPossessionVisitorTest {
  private static final VariableVisitor variableVisitor = new VariableVisitor();
  private static final GroupPossessionVisitor subject = new GroupPossessionVisitor(variableVisitor);
  private static final LocalDate AJD = LocalDate.of(2025, JUNE, 23);
  private static final Compte COMPTE_PERSONNEL =
      new Compte("comptePersonnel", AJD, ariary(1_000_000));
  private static final AchatMaterielAuComptant ACHAT_MATERIEL =
      new AchatMaterielAuComptant(
          "Ordinateur Portable",
          LocalDate.of(2025, JUNE, 15),
          ariary(500_000),
          10.0,
          COMPTE_PERSONNEL);
  private static final GroupePossession GROUPE_POSSESSION =
      new GroupePossession("mesPossessions", MGA, AJD, Set.of(COMPTE_PERSONNEL, ACHAT_MATERIEL));

  private GroupePossession visitGroupPossession(String input) {
    UnitTestVisitor visitor =
        new UnitTestVisitor() {
          @Override
          public GroupePossession visitSousTitre(SousTitreContext ctx) {
            return subject.apply(ctx, GROUPE_POSSESSION.getPossessions());
          }
        };
    return visitor.visit(input, PatriLangParser::sousTitre);
  }

  static {
    variableVisitor.addToScope("comptePersonnel", TRESORERIES, COMPTE_PERSONNEL);
  }

  @Test
  void parser_group_possession() {
    var input =
        """
        ### `mesPossessions`, le 23 du 06-2025, devise en Ar
        - `monCompte`, valant 1000000Ar le 01 du 01-2025
        - `achatOrdinateur` le 15 du 06-2025, acheter ordinateur, valant 500000Ar, depuis Trésoreries:comptePersonnel, s'appréciant annuellement de 10%
""";

    var actual = visitGroupPossession(input);

    assertEquals(GROUPE_POSSESSION, actual);
  }
}
