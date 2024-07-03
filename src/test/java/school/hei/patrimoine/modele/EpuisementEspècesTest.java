package school.hei.patrimoine.modele;

import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class EpuisementEspècesTest {

    public static void main(String[] args) {

        Patrimoine patrimoineZety = createPatrimoineInitial();

        ajouterEvenementsFinanciers(patrimoineZety);

        // Simulation jusqu'à l'épuisement des espèces
        LocalDate dateSimulation = LocalDate.of(2024, 9, 17);
        int montantMensuel = 250000; // Dépenses mensuelles à partir du 1er octobre 2024

        while (true) {
            Patrimoine patrimoineFutur = patrimoineZety.projectionFuture(dateSimulation);

            // Vérifier le solde en espèces
            Argent compteBancaire = (Argent) patrimoineFutur.possessionParNom("Compte bancaire de Zety");
            if (compteBancaire.getValeurComptable() <= 0) {
                System.out.println("Zety n'a plus d'espèces à partir du : " + dateSimulation);
                break;
            }

            // Avancer d'un jour pour la prochaine projection
            dateSimulation = dateSimulation.plusDays(1);
        }
    }

    private static Patrimoine createPatrimoineInitial() {
        Set<Possession> possessions = new HashSet<>();
        // Ajouter les possessions initiales de Zety si nécessaire
        possessions.add(new Argent("Compte bancaire de Zety", LocalDate.of(2024, 9, 17), 1000000));
        return new Patrimoine("Patrimoine de Zety", new Personne("Zety"), LocalDate.of(2024, 9, 17), possessions);
    }

    private static void ajouterEvenementsFinanciers(Patrimoine patrimoineZety) {

        // Paiement des frais de scolarité
        LocalDate datePaiementFrais = LocalDate.of(2024, 9, 21);
        FluxArgent paiementFrais = new FluxArgent(
                "Paiement frais de scolarité 2024-2025",
                new Argent("Compte bancaire de Zety", datePaiementFrais, 0),
                datePaiementFrais,
                datePaiementFrais,
                -2500000, // Montant du paiement des frais de scolarité
                datePaiementFrais.getDayOfMonth()
        );
        patrimoineZety.getPossessions().add(paiementFrais);

        // Transfert mensuel d'argent
        LocalDate debutTransferts = LocalDate.of(2024, 1, 1); // À partir de début 2024
        LocalDate finTransferts = LocalDate.of(2025, 2, 13); // Jusqu'au 13 février 2025
        FluxArgent transfertMensuel = new FluxArgent(
                "Transfert mensuel d'argent",
                new Argent("Espèces de Zety", debutTransferts, 0),
                debutTransferts,
                finTransferts,
                100000, // Montant mensuel de transfert
                15 // Transfert le 15 de chaque mois
        );
        patrimoineZety.getPossessions().add(transfertMensuel);

        // Dépenses mensuelles à partir du 1er octobre 2024
        LocalDate debutDepenses = LocalDate.of(2024, 10, 1); // À partir du 1er octobre 2024
        int montantMensuel = 250000; // Initialisation de la variable montantMensuel
        FluxArgent depensesMensuelles = new FluxArgent(
                "Dépenses mensuelles de Zety",
                new Argent("Espèces de Zety", debutDepenses, 0),
                debutDepenses,
                LocalDate.of(2025, 2, 13), // Jusqu'au 13 février 2025
                -montantMensuel, // Montant des dépenses mensuelles
                1
        );
        patrimoineZety.getPossessions().add(depensesMensuelles);
    }
}
