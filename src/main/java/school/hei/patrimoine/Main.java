package school.hei.patrimoine;

import school.hei.patrimoine.modele.*;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        // Création de Zety en tant que personne
        Personne zety = new Personne("Zety");

        // Date de référence
        LocalDate au3juillet24 = LocalDate.of(2024, 7, 3);
        LocalDate au17septembre24 = LocalDate.of(2024, 9, 17);

        // Création des possessions de Zety
        Argent compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
        Argent especes = new Argent("Espèces", au3juillet24, 800_000);

        AchatMaterielAuComptant ordinateur = new AchatMaterielAuComptant(
                "Ordinateur",
                au3juillet24,
                1_200_000,
                -10.0 / 365,
                especes);

        Materiel vetements = new Materiel(
                "Vêtements",
                au3juillet24,
                1_500_000,
                au3juillet24,
                -50.0 / 365);

        FluxArgent fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                especes,
                LocalDate.of(2023, 11, 27),
                LocalDate.of(2024, 8, 27),
                -200_000,
                27);

        FluxArgent fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                LocalDate.of(2024, 7, 25),
                LocalDate.of(2100, 1, 1),
                -20_000,
                25);

        // Création du patrimoine initial de Zety au 3 juillet 2024
        Patrimoine patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineZetyAu3juillet24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, especes, compteBancaire, fraisScolarite, fraisTenueCompte));

        // Calcul de la valeur du patrimoine de Zety au 17 septembre 2024
        EvolutionPatrimoine evolutionPatrimoine = new EvolutionPatrimoine(
                "Evolution de Zety",
                patrimoineZetyAu3juillet24,
                au3juillet24,
                au17septembre24);

        // Obtention de la valeur comptable du patrimoine au 17 septembre 2024
        int valeurPatrimoineAu17Septembre = evolutionPatrimoine.getEvolutionJournaliere().get(au17septembre24).getValeurComptable();

        // Affichage du résultat
        System.out.println("La valeur du patrimoine de Zety au 17 septembre 2024 est : " + valeurPatrimoineAu17Septembre);
    }
}
