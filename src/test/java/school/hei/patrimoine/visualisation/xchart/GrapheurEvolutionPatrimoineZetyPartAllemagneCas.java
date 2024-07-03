package school.hei.patrimoine.cas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.TauxChange;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineEtudiantZetyCasTest {

    private Patrimoine patrimoineZety;
    private TauxChange tauxChangeEurAr;

    @BeforeEach
    void setUp() {
        PatrimoineEtudiantZetyCas zetyCas = new PatrimoineEtudiantZetyCas();
        patrimoineZety = zetyCas.get();
        tauxChangeEurAr = new TauxChange(Devise.EUR, Devise.AR, LocalDate.of(2024, 7, 3), 4821, -0.1);
    }

    @Test
    void testValeurPatrimoine14Fevrier2025() {
        LocalDate date = LocalDate.of(2025, 2, 14);
        double valeur = patrimoineZety.valeur(date, Devise.AR);
        System.out.println("Valeur du patrimoine le 14 février 2025 : " + valeur + " Ar");
        // Expected value for Patrimoibne 14 Fevrier 2025
    }

    @Test
    void testValeurPatrimoine26Octobre2025() {
        LocalDate date = LocalDate.of(2025, 10, 26);
        double valeurAr = patrimoineZety.valeur(date, Devise.AR);
        double tauxChange = tauxChangeEurAr.getTaux(date);
        double valeurEur = valeurAr / tauxChange;
        System.out.println("Valeur du patrimoine le 26 octobre 2025 : " + valeurEur + " €");
        // Expected value for Patrimoibne 26 octobre 2025
    }
}
