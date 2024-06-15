package school.hei.patrimoine.serialisation;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.MAY;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SerialiseurTest {

  @Test
  void serialise_et_deserialise() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        -100_000,
        au13mai24.minusDays(100),
        au13mai24.plusDays(100),
        financeur, 15);
    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie));

    var serialiseur = new Serialiseur<Patrimoine>();
    var serialisé = serialiseur.serialise(patrimoineIloAu13mai24);

    assertEquals(patrimoineIloAu13mai24, serialiseur.deserialise(serialisé));
  }
}