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
  void debug_valeurMarche_apres_projection() {
    var t0 = LocalDate.of(2025, JANUARY, 1);
    var tVm = LocalDate.of(2025, MARCH, 1);
    var tFut = LocalDate.of(2025, JUNE, 1);

    var materiel = new Materiel("Ordi", t0, t0, ariary(10_000_000), 0.0);
    new ValeurMarche(materiel, tVm, ariary(52_000_000));

    System.out.println("Avant projection: " + materiel.getValeurMarche(tFut));
    System.out.println("Historique avant: " + materiel.historiqueValeurMarche().size());

    var projection = (Materiel) materiel.projectionFuture(tFut);

    System.out.println("Après projection: " + projection.getValeurMarche(tFut));
    System.out.println("Historique après: " + projection.historiqueValeurMarche().size());

    assertEquals(ariary(52_000_000), projection.getValeurMarche(tFut));
  }

  @Test
  void debug_valeurMarche_patrimoine_projete() {
    var t0 = LocalDate.of(2025, JANUARY, 1);
    var tVm = LocalDate.of(2025, MARCH, 1);
    var tFut = LocalDate.of(2025, JUNE, 1);

    var materiel = new Materiel("Ordi", t0, t0, ariary(10_000_000), 0.0);
    new ValeurMarche(materiel, tVm, ariary(52_000_000));

    var patrimoine = Patrimoine.of("test", MGA, t0, new Personne("Dyh"), Set.of(materiel));

    System.out.println("Patrimoine t0 VM: " + patrimoine.getValeurMarche());

    var projection = patrimoine.projectionFuture(tFut);
    System.out.println("Patrimoine projeté VM: " + projection.getValeurMarche());

    projection
        .getPossessions()
        .forEach(
            p ->
                System.out.println(
                    p.nom()
                        + " → historique: "
                        + p.historiqueValeurMarche().size()
                        + " → getValeurMarche: "
                        + p.getValeurMarche(tFut)));
  }

  @Test
  void debug_meme_instance_dans_patrimoine() {
    var t0 = LocalDate.of(2025, JANUARY, 1);
    var tVm = LocalDate.of(2025, MARCH, 1);

    var materiel = new Materiel("Ordi", t0, t0, ariary(10_000_000), 0.0);
    var patrimoine = Patrimoine.of("test", MGA, t0, new Personne("Dyh"), Set.of(materiel));

    new ValeurMarche(materiel, tVm, ariary(52_000_000));

    System.out.println("materiel id: " + System.identityHashCode(materiel));
    patrimoine
        .getPossessions()
        .forEach(
            p ->
                System.out.println(
                    p.nom()
                        + " id: "
                        + System.identityHashCode(p)
                        + " historique: "
                        + p.historiqueValeurMarche().size()));
  }
}
