package school.hei.patrimoine.modele.vente;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Materiel;

class VMGraphDebugTest {
  @Test
  void valeurMarche_apres_projection_materiel_retourne_valeur_marche() {
    var t0 = LocalDate.of(2025, JANUARY, 1);
    var tVm = LocalDate.of(2025, MARCH, 1);
    var tFut = LocalDate.of(2025, JUNE, 1);

    var materiel = new Materiel("Ordi", t0, t0, ariary(10_000_000), 0.0);
    new ValeurMarche(materiel, tVm, ariary(52_000_000));

    assertEquals(ariary(52_000_000), materiel.getValeurMarche(tFut));
    assertEquals(1, materiel.historiqueValeurMarche().size());

    var projection = (Materiel) materiel.projectionFuture(tFut);
    assertEquals(ariary(52_000_000), projection.getValeurMarche(tFut));
    assertEquals(1, projection.historiqueValeurMarche().size());
  }

  @Test
  void valeurMarche_patrimoine_projete_retourne_valeur_marche_correcte() {
    var t0 = LocalDate.of(2025, JANUARY, 1);
    var tVm = LocalDate.of(2025, MARCH, 1);
    var tFut = LocalDate.of(2025, JUNE, 1);

    var materiel = new Materiel("Ordi", t0, t0, ariary(10_000_000), 0.0);
    new ValeurMarche(materiel, tVm, ariary(52_000_000));

    var patrimoine = Patrimoine.of("test", MGA, t0, new Personne("Dyh"), Set.of(materiel));

    assertEquals(ariary(10_000_000), patrimoine.getValeurMarche());

    var projection = patrimoine.projectionFuture(tFut);

    assertEquals(ariary(52_000_000), projection.getValeurMarche());

    var ordiProjecte =
        projection.getPossessions().stream()
            .filter(p -> p.nom().equals("Ordi"))
            .findFirst()
            .orElseThrow();
    assertEquals(1, ordiProjecte.historiqueValeurMarche().size());
    assertEquals(ariary(52_000_000), ordiProjecte.getValeurMarche(tFut));
  }

  @Test
  void meme_instance_materiel_dans_patrimoine_apres_ajout_valeurMarche() {
    var t0 = LocalDate.of(2025, JANUARY, 1);
    var tVm = LocalDate.of(2025, MARCH, 1);

    var materiel = new Materiel("Ordi", t0, t0, ariary(10_000_000), 0.0);
    var patrimoine = Patrimoine.of("test", MGA, t0, new Personne("Dyh"), Set.of(materiel));

    new ValeurMarche(materiel, tVm, ariary(52_000_000));
    var ordiDansPatrimoine =
        patrimoine.getPossessions().stream()
            .filter(p -> p.nom().equals("Ordi"))
            .findFirst()
            .orElseThrow();

    assertEquals(System.identityHashCode(materiel), System.identityHashCode(ordiDansPatrimoine));

    assertEquals(1, ordiDansPatrimoine.historiqueValeurMarche().size());
  }
}
