package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EvolutionPatrimoineTest {

    @Test
    void patrimoine_evolue() {
        var ilo = new Personne("Ilo");
        var au13mai24 = LocalDate.of(2024, MAY, 13);
        var financeur = new Argent("Espèces", au13mai24, 600_000);
        var trainDeVie = new FluxArgent(
                "Vie courante",
                financeur, au13mai24.minusDays(100), au13mai24.plusDays(100), -100_000,
                15);
        var patrimoineIloAu13mai24 = new Patrimoine(
                "patrimoineIloAu13mai24",
                ilo,
                au13mai24,
                Set.of(financeur, trainDeVie));

        var evolutionPatrimoine = new EvolutionPatrimoine(
                "Nom",
                patrimoineIloAu13mai24,
                LocalDate.of(2024, MAY, 12),
                LocalDate.of(2024, MAY, 17));

        var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();
        assertEquals(0, evolutionJournaliere.get(LocalDate.of(2024, MAY, 12)).getValeurComptable());
        assertEquals(600_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 13)).getValeurComptable());
        assertEquals(600_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 14)).getValeurComptable());
        assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 15)).getValeurComptable());
        assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 16)).getValeurComptable());
        assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 17)).getValeurComptable());
    }

    @Test
    public void patrimoine_zety_2023_2024() {
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        
        var ordinateur = new Materiel("ordinateur", au3juillet24, 1_200_000, au3juillet24, -10);
        var vetements = new Materiel("vetements", au3juillet24, 1_500_000, au3juillet24, -50);
        var especes = new Argent("espèces", au3juillet24, 800_000);

        var novembre23 = LocalDate.of(2023, NOVEMBER, 1);
        var aout24 = LocalDate.of(2024, AUGUST, 31);
        var fraisScolarite = new FluxArgent("frais de scolarité", especes, novembre23, aout24, -200_000, 27);

        var compteBancaire=new Argent("compte bancaire",au3juillet24,100_000);
        var dateIndetermine= LocalDate.MAX;
        var fraisDeCompte=new FluxArgent("frais de tenue de compte",compteBancaire,au3juillet24,dateIndetermine,-20_000,25);


        var patrimoineZety= new Patrimoine("patrimoine de Zety",zety,au3juillet24,Set.of(ordinateur,vetements,especes,fraisScolarite,compteBancaire,fraisDeCompte));
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

        var au2juillet24=LocalDate.of(2024,JULY,2);
        var evolutionPatrimoineZety= new EvolutionPatrimoine("patrimoine evolue",patrimoineZety,au2juillet24,au17septembre24);

        var evolutionJournaliere = evolutionPatrimoineZety.getEvolutionJournaliere();
        assertEquals(0,evolutionJournaliere.get(LocalDate.of(2024,JULY,2)).getValeurComptable());
       assertEquals(patrimoineZety.getValeurComptable(),evolutionJournaliere.get(au3juillet24).getValeurComptable());
       assertEquals(460_000,evolutionJournaliere.get(au17septembre24).getValeurComptable());


    }
}