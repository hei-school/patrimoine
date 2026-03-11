package school.hei.patrimoine.patrilang.unit.modele;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;
import static school.hei.patrimoine.patrilang.utils.Comparator.assertFluxArgentEquals;
import static school.hei.patrimoine.patrilang.utils.UnitTestVisitor.createParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.modele.template.OperationTemplate;
import school.hei.patrimoine.patrilang.modele.template.OperationTemplateParam;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;

class OperationTemplateTest {
  @Test
  void throws_if_args_more_than_params() {
    var operationTemplate =
        new OperationTemplate("charges", List.of(), List.of(mock(OperationsContext.class)));
    var parentScope = new VariableScope();
    List<Object> args = List.of(new Personne("john"));

    var error =
        assertThrows(
            IllegalArgumentException.class, () -> operationTemplate.apply(parentScope, args));

    assertEquals(
        "Erreur au niveau du constructeurs d'opérations name=charges. Le nombre d'arguments fournis"
            + " ne peut pas dépasser celui attendu par le template.",
        error.getMessage());
  }

  @Test
  void apply_correct_arguments() {
    var formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");

    var ajd = LocalDate.of(2025, 2, 5);
    var dateFin = LocalDate.of(2025, 5, 24);
    var comptePersonnel = new Compte("comptePersonnel", ajd, ariary(200_000));

    var parentScope = new VariableScope();
    parentScope.add("ajd", DATE, ajd);

    var templateContentInput =
        "* `abonnementWifi + Dates:ajd` Dates:ajd, sortir 40000Ar depuis Trésoreries:compte,"
            + " jusqu'à Dates:dateFin tous les 15 du mois";
    var parser = createParser(templateContentInput);

    var templateParams =
        List.of(
            new OperationTemplateParam("dateFin", DATE),
            new OperationTemplateParam("compte", TRESORERIES));

    var subject = new OperationTemplate("charges", templateParams, List.of(parser.operations()));

    var operations = subject.apply(parentScope, List.of(dateFin, comptePersonnel));
    var expected =
        new FluxArgent(
            "abonnementWifi" + ajd.format(formatter),
            comptePersonnel,
            ajd,
            dateFin,
            15,
            ariary(-40_000));
    for (var operation : operations) {
      var actual = (FluxArgent) operation;
      assertFluxArgentEquals(expected, actual);
    }
  }
}
