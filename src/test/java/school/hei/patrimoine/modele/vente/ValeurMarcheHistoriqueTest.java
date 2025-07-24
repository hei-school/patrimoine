package school.hei.patrimoine.modele.vente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;

public class ValeurMarcheHistoriqueTest {
  private Materiel materiel;
  private LocalDate aujourdhui;
  private Argent valeurInitiale;

  @BeforeEach
  void setUp() {
    aujourdhui = LocalDate.now();
    valeurInitiale = new Argent(1000, Devise.EUR);
    materiel = new Materiel("Ordinateur", aujourdhui, aujourdhui, valeurInitiale, 0.05);
  }

  @Test
  void historique_valeur_marche_retourne_copie_defensive() {
    Set<ValeurMarche> copie = materiel.historiqueValeurMarche();
    copie.clear();

    assertEquals(1, materiel.historiqueValeurMarche().size());
    assertTrue(copie.isEmpty());
  }

  @Test
  void historique_valeur_marche_contient_valeur_initiale() {
    Set<ValeurMarche> historique = materiel.historiqueValeurMarche();

    assertEquals(1, historique.size());
    ValeurMarche premiereValeur = historique.iterator().next();
    assertEquals(aujourdhui, premiereValeur.t());
    assertEquals(valeurInitiale, premiereValeur.valeur());
  }

  @Test
  void historique_valeur_marche_apres_ajout_nouvelle_valeur() {
    LocalDate demain = aujourdhui.plusDays(1);
    Argent nouvelleValeur = new Argent(1050, Devise.EUR);
    materiel.ajouterValeurMarche(new ValeurMarche(demain, nouvelleValeur));

    Set<ValeurMarche> historique = materiel.historiqueValeurMarche();

    assertEquals(2, historique.size());
    assertTrue(historique.contains(new ValeurMarche(aujourdhui, valeurInitiale)));
    assertTrue(historique.contains(new ValeurMarche(demain, nouvelleValeur)));
  }

  @Test
  void historique_valeur_marche_pour_type_non_eligible() {
    Compte compte = new Compte("Compte Courant", aujourdhui, valeurInitiale);

    Set<ValeurMarche> historique = compte.historiqueValeurMarche();

    assertEquals(1, historique.size());
    assertEquals(valeurInitiale, historique.iterator().next().valeur());
  }
}
