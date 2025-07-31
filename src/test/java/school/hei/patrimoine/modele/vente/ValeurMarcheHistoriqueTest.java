package school.hei.patrimoine.modele.vente;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;

public class ValeurMarcheHistoriqueTest {

  @Test
  void historique_valeur_marche_contient_valeur_initiale() {
    var aujourdhui = LocalDate.now();
    var valeurInitiale = new Argent(1000, Devise.EUR);
    var materiel = new Materiel("Ordinateur", aujourdhui, aujourdhui, valeurInitiale, 0.05);
    new ValeurMarche(materiel, aujourdhui, valeurInitiale);

    var historique = materiel.historiqueValeurMarche();

    assertEquals(1, historique.size());
    var premiereValeur = historique.iterator().next();
    assertEquals(materiel, premiereValeur.possession());
    assertEquals(aujourdhui, premiereValeur.t());
    assertEquals(valeurInitiale, premiereValeur.valeur());
  }

  @Test
  void historique_valeur_marche_apres_ajout_nouvelle_valeur() {
    var aujourdhui = LocalDate.now();
    var valeurInitiale = new Argent(1000, Devise.EUR);
    var materiel = new Materiel("Ordinateur", aujourdhui, aujourdhui, valeurInitiale, 0.05);
    new ValeurMarche(materiel, aujourdhui, valeurInitiale);

    var demain = aujourdhui.plusDays(1);
    var nouvelleValeur = new Argent(1050, Devise.EUR);

    materiel.ajouterValeurMarche(new ValeurMarche(materiel, demain, nouvelleValeur));

    var historique = materiel.historiqueValeurMarche();
    assertEquals(2, historique.size());
    assertTrue(historique.contains(new ValeurMarche(materiel, aujourdhui, valeurInitiale)));
    assertTrue(historique.contains(new ValeurMarche(materiel, demain, nouvelleValeur)));
  }

  @Test
  void historique_valeur_marche_pour_type_non_eligible() {
    var aujourdhui = LocalDate.now();
    var valeurInitiale = new Argent(1000, Devise.EUR);
    var compte = new Compte("Compte Courant", aujourdhui, valeurInitiale);

    var exception =
        assertThrows(
            UnsupportedOperationException.class,
            () -> compte.ajouterValeurMarche(new ValeurMarche(compte, aujourdhui, valeurInitiale)));

    assertEquals(
        "Seules les IMMOBILISATIONs et ENTREPRISEs peuvent avoir une valeur de marché",
        exception.getMessage());
    assertEquals(0, compte.historiqueValeurMarche().size());
  }
}
