package school.hei.patrimoine.modele.vente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Materiel;

class ValeurMarcheHistoriqueTest {
  private static final LocalDate T0 = LocalDate.of(2024, 1, 1);
  private static final LocalDate T1 = LocalDate.of(2024, 6, 1);
  private static final LocalDate T2 = LocalDate.of(2024, 9, 1);
  private static final LocalDate T3 = LocalDate.of(2025, 1, 1);

  private static final Argent VALEUR_COMPTABLE = new Argent(10_000, Devise.EUR);
  private static final Argent VALEUR_MARCHE_1 = new Argent(12_000, Devise.EUR);
  private static final Argent VALEUR_MARCHE_2 = new Argent(15_000, Devise.EUR);

  private Materiel materiel;

  @BeforeEach
  void setUp() {
    materiel = new Materiel("Voiture", T0, T0, VALEUR_COMPTABLE, 0.0);
  }

  @Test
  void historiqueValeurMarche_videParDefaut() {
    assertTrue(materiel.historiqueValeurMarche().isEmpty());
  }

  @Test
  void historiqueValeurMarche_contientToutesLesValeursAjoutees() {
    var vm1 = new ValeurMarche(materiel, T1, VALEUR_MARCHE_1);
    var vm2 = new ValeurMarche(materiel, T2, VALEUR_MARCHE_2);

    Set<ValeurMarche> historique = materiel.historiqueValeurMarche();

    assertEquals(2, historique.size());
    assertTrue(historique.contains(vm1));
    assertTrue(historique.contains(vm2));
  }

  @Test
  void getValeurMarche_sansAucuneVm_retourneValeurComptable() {
    assertEquals(VALEUR_COMPTABLE, materiel.getValeurMarche(T1));
  }

  @Test
  void getValeurMarche_exactementALaDate_retourneCetteValeur() {
    new ValeurMarche(materiel, T1, VALEUR_MARCHE_1);

    assertEquals(VALEUR_MARCHE_1, materiel.getValeurMarche(T1));
  }

  @Test
  void getValeurMarche_apresLaDate_retourneDerniereVmConnue() {
    new ValeurMarche(materiel, T1, VALEUR_MARCHE_1);

    assertEquals(VALEUR_MARCHE_1, materiel.getValeurMarche(T2));
  }

  @Test
  void getValeurMarche_avantLaDate_retourneValeurComptable() {
    new ValeurMarche(materiel, T2, VALEUR_MARCHE_2);

    assertEquals(VALEUR_COMPTABLE, materiel.getValeurMarche(T1));
  }

  @Test
  void getValeurMarche_plusieursVm_retourneLaPlusRecenteAvantOuEgaleT() {
    new ValeurMarche(materiel, T1, VALEUR_MARCHE_1);
    new ValeurMarche(materiel, T2, VALEUR_MARCHE_2);

    assertEquals(VALEUR_MARCHE_2, materiel.getValeurMarche(T3));
    assertEquals(VALEUR_MARCHE_1, materiel.getValeurMarche(T1.plusDays(1)));
  }

  @Test
  void getValeurMarche_exactementAT2AvecDeuxVm_retourneVm2() {
    new ValeurMarche(materiel, T1, VALEUR_MARCHE_1);
    new ValeurMarche(materiel, T2, VALEUR_MARCHE_2);

    assertEquals(VALEUR_MARCHE_2, materiel.getValeurMarche(T2));
  }
}
