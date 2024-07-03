package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Set;

import static java.util.Calendar.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZetySEndetteTest {
    @Test
    void testDiminutionPatrimoine() {
        // Création du patrimoine de Zety au 17 septembre 2024
        LocalDate dateAvantPret = LocalDate.of(2024, 9, 17);
        Personne zety = new Personne("Zety");
        Argent compteBancaireAvantPret = new Argent("Compte Bancaire", dateAvantPret, 0);
        Patrimoine patrimoineAvantPret = new Patrimoine("Zety (avant prêt)", zety, dateAvantPret, Set.of(compteBancaireAvantPret));

        // Création du patrimoine de Zety au 18 septembre 2024 après le prêt
        LocalDate dateApresPret = LocalDate.of(2024, 9, 18);
        Argent compteBancaireApresPret = new Argent("Compte Bancaire", dateAvantPret, dateApresPret, 10_000_000);
        Dette detteApresPret = new Dette("Dette Bancaire", dateApresPret, -11_000_000);
        Set<Possession> possessionsApresPret = Set.of(compteBancaireApresPret, detteApresPret);
        Patrimoine patrimoineApresPret = new Patrimoine("Zety (après prêt)", zety, dateApresPret, possessionsApresPret);

        // Calcul de la diminution de patrimoine
        int valeurPatrimoineAvantPret = patrimoineAvantPret.getValeurComptable();
        int valeurPatrimoineApresPret = patrimoineApresPret.getValeurComptable();
        int diminutionPatrimoine = valeurPatrimoineAvantPret - valeurPatrimoineApresPret;

        // Vérification que le patrimoine a diminué de 1 000 000 Ar
        assertEquals(1_000_000, diminutionPatrimoine);
    }
}
