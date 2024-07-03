package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        var compteBancaire = new Argent("compte bancaire", au3juillet24, 100_000);
        var dateIndetermine = LocalDate.MAX;
        var fraisDeCompte = new FluxArgent("frais de tenue de compte", compteBancaire, au3juillet24, dateIndetermine, -20_000, 25);

        var ensemblePatrimoine = new HashSet<>(Set.of(ordinateur, vetements, especes, fraisScolarite, compteBancaire, fraisDeCompte));
        var patrimoineZety = new Patrimoine("patrimoine de Zety", zety, au3juillet24, ensemblePatrimoine);
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

        var au2juillet24 = LocalDate.of(2024, JULY, 2);
        var evolutionPatrimoineZety = new EvolutionPatrimoine("patrimoine evolue", patrimoineZety, au2juillet24, au17septembre24);

        var evolutionJournaliere = evolutionPatrimoineZety.getEvolutionJournaliere();
        assertEquals(0, evolutionJournaliere.get(LocalDate.of(2024, JULY, 2)).getValeurComptable());
        assertEquals(patrimoineZety.getValeurComptable(), evolutionJournaliere.get(au3juillet24).getValeurComptable());
        assertEquals(460_000, evolutionJournaliere.get(au17septembre24).getValeurComptable());


        var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);
        var empruntBanque = new FluxArgent("argent emprunte à la banque", compteBancaire, au18septembre24, au18septembre24, 10_000_000, au18septembre24.getDayOfMonth());

        var coutPret = 1_000_000;
        var dette = empruntBanque.getFluxMensuel() + coutPret;
        var au18septembre25 = au18septembre24.plusYears(1);
        var daysBetween = ChronoUnit.DAYS.between(au18septembre24, au18septembre25);
        var banqueDette = new Argent("dette banque", au18septembre25, dette);
        var endettement = new FluxArgent("argent à rendre à la banque", compteBancaire, au18septembre24, au18septembre25, -dette, au18septembre25.getDayOfMonth());

        ensemblePatrimoine.add(empruntBanque);
        ensemblePatrimoine.add(endettement);

        var evolutionPatrimoineZety18Septembre25 = new EvolutionPatrimoine("nom", patrimoineZety, au2juillet24, au18septembre25);
        var evolution = evolutionPatrimoineZety18Septembre25.getEvolutionJournaliere();
        var valeurDiminue=Math.abs(evolution.get(au18septembre24).getValeurComptable()-evolution.get(au17septembre24).getValeurComptable());
        assertEquals(1_000_000,valeurDiminue);




    }
}