package school.hei.patrimoine.Zety;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.HashMap;


import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Zety_etudie_en_2024_2025 {
    @Test
    void test_date_epuisement_especes() {
        LocalDate debut2024 = LocalDate.of(2024, JANUARY, 1);
        LocalDate finEtude = LocalDate.of(2025, FEBRUARY, 13);

        int donMensuel = 100_000;
        int depenseMensuelle = 250_000;

        int soldeEspeces = 0;

        for (Month mois = JANUARY; mois.getValue() <= SEPTEMBER.getValue(); mois = mois.plus(1)) {
            soldeEspeces += donMensuel;
        }

        for (Month mois = OCTOBER; mois.getValue() <= FEBRUARY.getValue(); mois = mois.plus(1)) {
            soldeEspeces += donMensuel;
            soldeEspeces -= depenseMensuelle;

            // Vérifier si le solde devient négatif ou nul
            if (soldeEspeces <= 0) {
                LocalDate dateEpuisement = LocalDate.of(2024, mois, 1);
                if (mois == FEBRUARY) {
                    dateEpuisement = LocalDate.of(2025, mois, 13);
                }
                assertEquals(LocalDate.of(2024, JANUARY, 1), dateEpuisement);
                break;
            }
        }
    }
}
