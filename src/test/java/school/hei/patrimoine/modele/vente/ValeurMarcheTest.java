package school.hei.patrimoine.modele.vente;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;

class ValeurMarcheTest {
  private static final LocalDate T0 = LocalDate.of(2024, 1, 1);
  private static final LocalDate T1 = LocalDate.of(2024, 6, 1);

  private static final Argent VALEUR_COMPTABLE = new Argent(10_000, Devise.EUR);
  private static final Argent VALEUR_MARCHE_1 = new Argent(12_000, Devise.EUR);

  private Compte compte;
  private Materiel materiel;

  @BeforeEach
  void setUp() {
    compte = new Compte("Compte courant", T0, new Argent(0, Devise.EUR));
    materiel = new Materiel("Voiture", T0, T0, VALEUR_COMPTABLE, 0.0);
  }

  @Test
  void valeurMarche_champsCorrectementInitialises() {
    var vm = new ValeurMarche(materiel, T1, VALEUR_MARCHE_1);

    assertEquals(materiel, vm.possession());
    assertEquals(T1, vm.t());
    assertEquals(VALEUR_MARCHE_1, vm.valeur());
  }

  @Test
  void valeurMarche_estAutomatiquementAjouteeALaPossession() {
    var vm = new ValeurMarche(materiel, T1, VALEUR_MARCHE_1);

    assertTrue(materiel.historiqueValeurMarche().contains(vm));
  }

  @Test
  void valeurMarche_surImmobilisation_neLeveAucuneException() {
    assertDoesNotThrow(() -> new ValeurMarche(materiel, T1, VALEUR_MARCHE_1));
  }

  @Test
  void valeurMarche_surTresorerie_neLeveAucuneException() {
    assertDoesNotThrow(() -> new ValeurMarche(compte, T1, VALEUR_MARCHE_1));
  }
}
