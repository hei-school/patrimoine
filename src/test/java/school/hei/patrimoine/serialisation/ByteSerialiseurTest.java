package school.hei.patrimoine.serialisation;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

class ByteSerialiseurTest {

  @Test
  void serialise_et_deserialise() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Compte("Espèces", au13mai24, ariary(600_000));
    var trainDeVie =
        new FluxArgent(
            "Vie courante",
            financeur,
            au13mai24.minusDays(100),
            au13mai24.plusDays(100),
            15,
            ariary(-100_000));
    var patrimoineIloAu13mai24 =
        Patrimoine.of("patrimoineIloAu13mai24", MGA, au13mai24, ilo, Set.of(financeur, trainDeVie));

    var serialiseur = new ByteSerialiseur<Patrimoine>();
    var serialisé = serialiseur.serialise(patrimoineIloAu13mai24);

    var désérialisé = serialiseur.deserialise(serialisé);
    assertEquals(patrimoineIloAu13mai24, désérialisé);
    var argentDésérialisé = désérialisé.possessionParNom("Espèces");
    assertEquals(
        argentDésérialisé, ((FluxArgent) désérialisé.possessionParNom("Vie courante")).getCompte());
    assertEquals( // car Possession::equals est symbolique
        argentDésérialisé, patrimoineIloAu13mai24.possessionParNom("Espèces"));
  }
}
