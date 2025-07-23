package school.hei.patrimoine.modele.vente;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

public class VenteTest {
     @Test
    void vendre_possession(){
         var dateAcquisition = LocalDate.now().minusDays(1);
         var t = LocalDate.now();
         var valeur = Argent.ariary(2000);
         var donut = new Materiel(
                 "donut",
                 dateAcquisition,
                 t,
                 valeur,
                 0
         );

         var compteBeneficiaire = new Compte(
                 "porte feuille",
                 dateAcquisition.minusDays(1),
                 Argent.ariary(0)
         );

         new Vente(
                 donut,
                 valeur,
                 t.plusDays(1),
                 compteBeneficiaire
         );

         assertEquals(Argent.ariary(2000) , compteBeneficiaire.valeurComptableFuture(t.plusDays(2)));
         assertEquals(Argent.ariary(0), donut.valeurComptableFuture(t.plusDays(2)));
     }
}
