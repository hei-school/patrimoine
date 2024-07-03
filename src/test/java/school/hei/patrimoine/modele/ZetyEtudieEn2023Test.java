package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.ZetyEtudieEn2023;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ZetyEtudieEn2023Test {

    @Test
    void testGet() {
        ZetyEtudieEn2023 zety = new ZetyEtudieEn2023();
        Patrimoine patrimoine = zety.get();

        assertEquals("Zety", patrimoine.nom());
        assertEquals("Zety", patrimoine.possesseur().nom());
        assertEquals(6, patrimoine.possessions().size());
    }

    @Test
    void patrimoine_zety_evolue(){
        ZetyEtudieEn2023 zety = new ZetyEtudieEn2023();
        Patrimoine patrimoine = zety.get();

        var evolutionPatrimoine = new EvolutionPatrimoine(
                "Zety",
                patrimoine,
                LocalDate.of(2023, 7, 3),
                LocalDate.of(2024, 9, 17)
        );
        var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();
        assertEquals(0, evolutionJournaliere.get(LocalDate.of(2023, 7, 3)).getValeurComptable());
        assertEquals(2978848, evolutionJournaliere.get(LocalDate.of(2024, 9, 17)).getValeurComptable());

    }

    @Test
    void zety_s_endette() {
        ZetyEtudieEn2023 zety = new ZetyEtudieEn2023();
        Patrimoine patrimoine = zety.get();

        LocalDate debutDette = LocalDate.of(2024, 9, 18);
        LocalDate finDette = debutDette.plusYears(1);
        LocalDate avantDette = debutDette.minusDays(1);

        EvolutionPatrimoine evolution = new EvolutionPatrimoine(
                "Evolution Zety",
                patrimoine,
                avantDette,
                finDette.plusDays(1)
        );
        int patrimoineAvantDette = evolution.getEvolutionJournaliere().get(avantDette).getValeurComptable();
        int patrimoineAprèsDette = evolution.getEvolutionJournaliere().get(debutDette).getValeurComptable();
        assertTrue( patrimoineAvantDette > patrimoineAprèsDette,
                "Le patrimoine devrait diminuer de 1M Ar juste après la dette");
        assertEquals(1976464, patrimoineAprèsDette);
    }
}