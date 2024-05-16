package school.hei.patrimoine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Materiel;
import school.hei.patrimoine.possession.Possession;
import school.hei.patrimoine.possession.TrainDeVie;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 =
        new Patrimoine(ilo, Instant.parse("2024-05-13T00:00:00.00Z"), Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var patrimoineIloAu13mai24 =
        new Patrimoine(
            ilo,
            au13mai24,
            Set.of(
                new Argent("Espèces", au13mai24, 400_000),
                new Argent("Compte epargne", au13mai24, 200_000),
                new Argent("Compte courant", au13mai24, 600_000)));

    assertEquals(1_200_000, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_possede_un_train_de_vie_financé_par_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 400_000);

    var trainDeVie = new TrainDeVie(null, 0, null, null, financeur, 0);

    var patrimoineIloAu13mai24 = new Patrimoine(ilo, au13mai24, Set.of(financeur, trainDeVie));
  }

  @Test
  void patrimoine_projeté_dans_une_heure() {
    var possesseur = new Personne("Ilo");

    var possession1 = new Argent("Vola", Instant.now(), 100_000);
    var possession2 = new Materiel("Matos", Instant.now(), 200_000, 0.10);

    var patrimoineDeIlo = new Patrimoine(possesseur, Instant.now(), Set.of(possession1, possession2));

    var patrimoineFutureDeIlo = patrimoineDeIlo.projectionFuture(Instant.now().plusSeconds(3600));

    assertEquals(patrimoineDeIlo.possesseur(), patrimoineFutureDeIlo.possesseur());
    assertEquals(patrimoineDeIlo.possessions().size(), patrimoineFutureDeIlo.possessions().size());
    assertTrue(patrimoineFutureDeIlo.t().isAfter(patrimoineDeIlo.t()));

    for (Possession possession: patrimoineDeIlo.possessions()) {
      assertTrue(patrimoineFutureDeIlo.possessions().contains(possession.projectionFuture(patrimoineFutureDeIlo.t())));
    }
  }
}
