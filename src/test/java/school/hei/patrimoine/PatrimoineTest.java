package school.hei.patrimoine;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Possession;
import school.hei.patrimoine.possession.TrainDeVie;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
class PatrimoineTest {
  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        Instant.parse("2024-05-13T00:00:00.00Z"),
        Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }
  @Test
  void patrimoine_a_de_l_argent_vaut_la_somme_de_valeur_comptable(){
    var ilo = new Personne("Ilo");
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        au13mai24,
        Set.of(
            new Argent("Espèces", au13mai24, 400_000),
            new Argent("Compte epargne", au13mai24, 200_000),
            new Argent("Compte courant", au13mai24, 600_000)));
    assertEquals(1_200_000,patrimoineIloAu13mai24.getValeurComptable());
  }
  @Test
  void patrimoine_vide_possede_un_train_de_vie_financé_par_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 400_000);

    var trainDeVie = new TrainDeVie(null, 0, null, null, financeur, 0);

    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie));
    assertEquals(400_000,patrimoineIloAu13mai24.getValeurComptable()-trainDeVie.getDepensesMensuelle());
  }
  @Test
  void patrimoine_possede_un_train_de_vie_financé_par_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 400_000);
    var trainDeVie = new TrainDeVie(null, 100_000, null, null, financeur, 0);
    var patrimoineIloAu13mai24 = new Patrimoine(
            ilo,
            au13mai24,
            Set.of(financeur, trainDeVie));
    assertEquals(300_000,patrimoineIloAu13mai24.getValeurComptable()-trainDeVie.getDepensesMensuelle());
  }
  @Test
  void patrimoine_projection_futur(){
    Personne ilo = new Personne("Ilo");
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var au26Oct21 = Instant.parse("2022-10-26T00:00:00.00Z");
    Set<Possession> possessions=new HashSet<>();
    Patrimoine patrimoine=new Patrimoine(ilo,au26Oct21,possessions);
    assertEquals(new Patrimoine(ilo,au13mai24,possessions),patrimoine.projectionFuture(au13mai24));
  }
}