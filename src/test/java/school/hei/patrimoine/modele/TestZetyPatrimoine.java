package school.hei.patrimoine.modele;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class TestZetyPatrimoine {

        @Test
        public void testValeurPatrimoine17Septembre2024() {
            ZetyPatrimoine zety = new ZetyPatrimoine();
            LocalDate dateInitiale = LocalDate.of(2024, 7, 3);
            LocalDate dateFinale = LocalDate.of(2024, 9, 17);
            double valeur = zety.calculerValeurPatrimoine(dateInitiale, dateFinale);
            System.out.println("Valeur du patrimoine au 17 septembre 2024: " + valeur);
            // Ajouter les assertions appropriées
        }

        @Test
        public void testEndettement18Septembre2024() {
            ZetyPatrimoine zety = new ZetyPatrimoine();
            LocalDate dateInitiale = LocalDate.of(2024, 9, 17);
            LocalDate dateFinale = LocalDate.of(2024, 9, 18);
            zety.sEndetter(10_000_000, dateFinale);
            double valeur = zety.calculerValeurPatrimoine(dateInitiale, dateFinale);
            System.out.println("Valeur du patrimoine au 18 septembre 2024 après endettement: " + valeur);
        
        }

        @Test
        public void testEpuisementEspeces() {
            ZetyPatrimoine zety = new ZetyPatrimoine();
            LocalDate date = LocalDate.of(2024, 10, 1);
            while (zety.argentEspeces > 0) {
                zety.recevoirDonParents(date);
                zety.payerTrainVieMensuel(date);
                date = date.plusMonths(1);
            }
            System.out.println("Date d'épuisement des espèces: " + date);
        }
           @Test
    public void testValeurPatrimoine14Fevrier2025() {
        ZetyPatrimoine zety = new ZetyPatrimoine();
        LocalDate dateInitiale = LocalDate.of(2024, 7, 3);
        LocalDate dateFinale = LocalDate.of(2025, 2, 14);
        double valeur = zety.calculerValeurPatrimoine(dateInitiale, dateFinale);
        System.out.println("Valeur du patrimoine au 14 février 2025: " + valeur);
        
    }

    @Test
    public void testEndettement15Fevrier2025() {
        ZetyPatrimoine zety = new ZetyPatrimoine();
        LocalDate dateInitiale = LocalDate.of(2025, 2, 14);
        LocalDate dateFinale = LocalDate.of(2025, 2, 15);
        zety.sEndetter(7_000 * 4821, 0, dateFinale);
        double valeur = zety.calculerValeurPatrimoine(dateInitiale, dateFinale);
        System.out.println("Valeur du patrimoine au 15 février 2025 après endettement: " + valeur);
        
    }

    @Test
    public void testValeurPatrimoineEnEuros26Octobre2025() {
        ZetyPatrimoine zety = new ZetyPatrimoine();
        LocalDate date = LocalDate.of(2025, 10, 26);
        double tauxDeChange = 4821; 
        double appreciationAnnuelle = -0.10; 
        double valeurEnEuros = zety.calculerValeurPatrimoineEnDevise(date, "EUR", tauxDeChange, appreciationAnnuelle);
        System.out.println("Valeur du patrimoine en euros au 26 octobre 2025: " + valeurEnEuros);
        
    }
    }

