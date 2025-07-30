package school.hei.patrimoine.modele.vente;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;

import static org.junit.jupiter.api.Assertions.*;

public class ValeurMarcheHistoriqueTest {

  @Test
  void historique_valeur_marche_contient_valeur_initiale() {
    // Given
    LocalDate aujourdhui = LocalDate.now();
    Argent valeurInitiale = new Argent(1000, Devise.EUR);
    Materiel materiel = new Materiel("Ordinateur", aujourdhui, aujourdhui, valeurInitiale, 0.05);
    new ValeurMarche(materiel, aujourdhui, valeurInitiale);

    // When
    Set<ValeurMarche> historique = materiel.historiqueValeurMarche();

    // Then
    assertEquals(1, historique.size());
    ValeurMarche premiereValeur = historique.iterator().next();
    assertEquals(materiel, premiereValeur.possession());
    assertEquals(aujourdhui, premiereValeur.t());
    assertEquals(valeurInitiale, premiereValeur.valeur());
  }

  @Test
  void historique_valeur_marche_retourne_copie_defensive() {
    // Given
    LocalDate aujourdhui = LocalDate.now();
    Argent valeurInitiale = new Argent(1000, Devise.EUR);
    Materiel materiel = new Materiel("Ordinateur", aujourdhui, aujourdhui, valeurInitiale, 0.05);
    new ValeurMarche(materiel, aujourdhui, valeurInitiale);

    // When
    Set<ValeurMarche> copie = materiel.historiqueValeurMarche();
    copie.clear();

    // Then
    assertEquals(1, materiel.historiqueValeurMarche().size());
    assertTrue(copie.isEmpty());
  }

  @Test
  void historique_valeur_marche_apres_ajout_nouvelle_valeur() {
    // Given
    LocalDate aujourdhui = LocalDate.now();
    Argent valeurInitiale = new Argent(1000, Devise.EUR);
    Materiel materiel = new Materiel("Ordinateur", aujourdhui, aujourdhui, valeurInitiale, 0.05);
    new ValeurMarche(materiel, aujourdhui, valeurInitiale);

    LocalDate demain = aujourdhui.plusDays(1);
    Argent nouvelleValeur = new Argent(1050, Devise.EUR);

    // When
    materiel.ajouterValeurMarche(new ValeurMarche(materiel, demain, nouvelleValeur));

    // Then
    Set<ValeurMarche> historique = materiel.historiqueValeurMarche();
    assertEquals(2, historique.size());
    assertTrue(historique.contains(new ValeurMarche(materiel, aujourdhui, valeurInitiale)));
    assertTrue(historique.contains(new ValeurMarche(materiel, demain, nouvelleValeur)));
  }

  @Test
  void historique_valeur_marche_pour_type_non_eligible() {
    // Given
    LocalDate aujourdhui = LocalDate.now();
    Argent valeurInitiale = new Argent(1000, Devise.EUR);
    Compte compte = new Compte("Compte Courant", aujourdhui, valeurInitiale);

    // When/Then
    assertThrows(UnsupportedOperationException.class, () -> {
      compte.ajouterValeurMarche(new ValeurMarche(compte, aujourdhui, valeurInitiale));
    });

    // Then
    assertEquals(0, compte.historiqueValeurMarche().size());
  }
}