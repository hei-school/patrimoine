package school.hei.patrimoine.Zety;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Zety_s_endette {
    @Test
    void diminution_du_patrimoine_avec_la_dette(){
        LocalDate dateAvant = LocalDate.of(2024, SEPTEMBER, 17);
        LocalDate dateDEndettemnt = LocalDate.of(2024, SEPTEMBER, 18);
        int augmentationCompteBancaire = 10_000_000;
        int montantDette = -11_000_000;
        int diminutionDuPatrimoine = augmentationCompteBancaire + montantDette ;
        int valeurPatrimooinedeZetyLe17Juillet2023 = 2_932_369;
        int valeurPatrimooinedeZetyLe18Juillet2023 = augmentationCompteBancaire + montantDette + valeurPatrimooinedeZetyLe17Juillet2023;

        System.out.println("Zety doit a la banque : " + montantDette);
        System.out.println("La nouvelle valeur du patrimoine de Zety apres l'endettement est de : " + valeurPatrimooinedeZetyLe18Juillet2023 );
        assertEquals(-1_000_000, diminutionDuPatrimoine);
    }
}
