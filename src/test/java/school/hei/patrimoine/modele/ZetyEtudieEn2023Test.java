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
}