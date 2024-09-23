package school.hei.patrimoine.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.PatrimoineRicheCas;
import school.hei.patrimoine.modele.Patrimoine;

class PatrimoineCompilerTest {
  @Test
  void convertit_un_string_en_une_liste_de_patrimoine() throws Exception {

    String code =
        """
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static java.time.Month.MAY;
import static java.util.Calendar.JUNE;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.TransfertArgent;

public class PatrimoineRicheCas implements Supplier<Patrimoine> {

  @Override
  public Patrimoine get() {
    var ilo = new Personne("Cresus");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var compteCourant = new Argent("BP", au13mai24.minusDays(1), au13mai24, 13_410);
    new FluxArgent(
        "Salaire",
        compteCourant,
        LocalDate.of(2023, JANUARY, 1),
        LocalDate.of(2026, DECEMBER, 31),
        4_800,
        3);
    var trainDeVie =
        new GroupePossession(
            "Train de vie",
            au13mai24,
            Set.of(
                new FluxArgent(
                    "Loyer",
                    compteCourant,
                    LocalDate.of(2023, JANUARY, 1),
                    LocalDate.of(2026, DECEMBER, 31),
                    -1_450,
                    27),
                new FluxArgent(
                    "Courses",
                    compteCourant,
                    LocalDate.of(2023, JANUARY, 1),
                    LocalDate.of(2026, DECEMBER, 31),
                    -1_100,
                    1)));

    var voiture =
        new AchatMaterielAuComptant(
            "Voiture", LocalDate.of(2025, JUNE, 4), 22_450, -0.4, compteCourant);
    var mac = new Materiel("MacBook Pro", au13mai24, 2_000, au13mai24, -0.9);

    var compteEpargne = new Argent("CE", LocalDate.of(2025, Calendar.SEPTEMBER, 7), 0);
    new TransfertArgent(
        "Salaire",
        compteCourant,
        compteEpargne,
        LocalDate.of(2025, DECEMBER, 1),
        LocalDate.of(2026, Calendar.JULY, 27),
        3_200,
        3);

    return Patrimoine.of(
        "Riche", ilo, au13mai24, Set.of(compteCourant, compteEpargne, trainDeVie, voiture, mac));
  }
}
""";

    PatrimoineRicheCas patrimoineRicheCas = new PatrimoineRicheCas();

    Patrimoine patrimoineRiche = patrimoineRicheCas.get();

    PatrimoineCompiler patrimoineCompiler = new PatrimoineCompiler();

    Patrimoine patrimoine = patrimoineCompiler.apply("PatrimoineRicheCas", code);

    assertEquals(patrimoineRiche.getValeurComptable(), patrimoine.getValeurComptable());
  }
}
