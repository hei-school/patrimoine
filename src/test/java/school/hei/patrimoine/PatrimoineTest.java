package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Materiel;
import school.hei.patrimoine.possession.TrainDeVie;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var patrimoineIloAu13mai24 = new Patrimoine(
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

    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie));
  }

  @Test
  void patrimoine_projeter_dans_futur(){
    var tsiory = new Personne("tsiory");
    var au16mai24 = Instant.parse("2024-05-16T00:00:00.00Z");
    var compteCourant = new Argent("compte courant" , au16mai24 , 500_000);
    var debut = Instant.parse("2024-05-16T00:00:00.00Z");
    var fin = Instant.parse("2025-05-16T00:00:00.00Z");
    var vieEtudiant = new TrainDeVie("vie etudiant" , 300_000 ,debut,fin,compteCourant, 2 );
    var materiel = new Materiel("ordinateur",au16mai24,2_000_000,10);

    var patrimoineau16mai24 = new Patrimoine(
            tsiory,
            au16mai24,
            Set.of(compteCourant,vieEtudiant,materiel)
    );

    var patrimoineFinEtude = patrimoineau16mai24.projectionFuture(fin);

    var finVieEtudiant = vieEtudiant.projectionFuture(fin);
    var argenFinEtude = compteCourant.projectionFuture(fin);
    var materielFinEtude = materiel.projectionFuture(fin);
    var possessionFutur = Set.of(finVieEtudiant,materielFinEtude,argenFinEtude);
    assertTrue(
         patrimoineFinEtude.possessions().containsAll(possessionFutur)
    );

  }
}