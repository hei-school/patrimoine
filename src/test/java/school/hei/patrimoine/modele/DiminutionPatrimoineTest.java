package school.hei.patrimoine.modele;

import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class DiminutionPatrimoineTest {

    public static void main(String[] args) {
        testDiminutionPatrimoine();
    }

    public static void testDiminutionPatrimoine() {
        Patrimoine patrimoineZety = createPatrimoineZety();

        // Simulation de l'opération d'endettement le 18 septembre 2024
        LocalDate dateOperation = LocalDate.of(2024, 9, 18);
        Dette dette = createDette(dateOperation);
        FluxArgent fluxArgent = createFluxArgent(dateOperation);

        // Ajouter la dette et le flux d'argent au patrimoine de Zety
        patrimoineZety.getPossessions().add(dette);
        patrimoineZety.getPossessions().add(fluxArgent);

        // Calculer la diminution du patrimoine de Zety entre le 17 et le 18 septembre 2024
        LocalDate dateAvant = LocalDate.of(2024, 9, 17);
        LocalDate dateApres = LocalDate.of(2024, 9, 18);

        int diminutionPatrimoine = calculerDiminutionPatrimoine(patrimoineZety, dateAvant, dateApres);

        // Afficher le résultat
        System.out.println("La diminution du patrimoine de Zety entre le 17 et le 18 septembre 2024 est de : " + diminutionPatrimoine + " Ar");
    }

    private static Patrimoine createPatrimoineZety() {
        Set<Possession> possessions = new HashSet<>();
        // Ajouter les possessions actuelles de Zety si nécessaire
        return new Patrimoine("Patrimoine de Zety", new Personne("Zety"), LocalDate.of(2024, 9, 17), possessions);
    }

    private static Dette createDette(LocalDate dateOperation) {
        int montantEmprunt = 10000000;
        int coutDuPret = 1000000;
        return new Dette("Dette bancaire", dateOperation, montantEmprunt, coutDuPret);
    }

    private static FluxArgent createFluxArgent(LocalDate dateOperation) {
        int montantEmprunt = 10000000;
        return new FluxArgent(
                "Emprunt bancaire de Zety",
                new Argent("Compte bancaire de Zety", dateOperation, 0),
                dateOperation,
                dateOperation,
                montantEmprunt,
                dateOperation.getDayOfMonth()
        );
    }

    private static int calculerDiminutionPatrimoine(Patrimoine patrimoine, LocalDate dateAvant, LocalDate dateApres) {
        int valeurComptableAvant = patrimoine.projectionFuture(dateAvant).getValeurComptable();
        int valeurComptableApres = patrimoine.projectionFuture(dateApres).getValeurComptable();
        return valeurComptableAvant - valeurComptableApres;
    }
}
