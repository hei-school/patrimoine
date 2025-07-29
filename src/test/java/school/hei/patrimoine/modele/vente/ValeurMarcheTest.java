package school.hei.patrimoine.modele.vente;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.vente.ValeurMarche;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ValeurMarcheTest {

    @Test
    public void valeurMarche_is_added_to_possession() {
        // GIVEN
        LocalDate date = LocalDate.of(2024, 1, 1);
        Argent valeurComptable = new Argent(new BigDecimal("1000"), Devise.MGA);
        Possession possession = new Materiel("ordi",valeurComptable, date);

        // WHEN : on crée une nouvelle valeur de marché liée à la possession
        Argent valeurMarche = new Argent(1200);
        ValeurMarche vm = new ValeurMarche(possession, date.plusDays(30), valeurMarche);

        // THEN : elle est automatiquement ajoutée à la possession
        Set<ValeurMarche> valeursMarche = possession.getValeursMarche();

        assertTrue(valeursMarche.contains(vm));
        assertEquals(2, valeursMarche.size()); // initiale + celle ajoutée
        assertTrue(valeursMarche.stream().anyMatch(
                v -> v.t().equals(date.plusDays(30)) && v.valeur().equals(valeurMarche)
        ));
    }
}

