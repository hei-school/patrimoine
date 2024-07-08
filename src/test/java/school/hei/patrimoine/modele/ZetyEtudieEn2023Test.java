package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.ZetyEtudieEn2023;
import school.hei.patrimoine.modele.possession.Argent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ZetyEtudieEn2023Test {

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
        //Zety obtient des dons parentaux de 300_000
        assertEquals(2978848, evolutionJournaliere.get(LocalDate.of(2024, 9, 17)).getValeurComptable() - 300_000);

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
        //Zety obtient des dons parentaux de 300_000
        assertEquals(1002384, patrimoineAvantDette-patrimoineAprèsDette);
    }

    @Test
    void zety_n_a_plus_d_argent() {
        ZetyEtudieEn2023 zetyCase = new ZetyEtudieEn2023();
        Patrimoine patrimoine = zetyCase.get();

        LocalDate debut = LocalDate.of(2024, 7, 3);
        LocalDate fin = LocalDate.of(2025, 12, 31);

        EvolutionPatrimoine evolution = new EvolutionPatrimoine(
                "Evolution Zety",
                patrimoine,
                debut,
                fin
        );

        List<LocalDate> dates = new ArrayList<>(evolution.getEvolutionJournaliere().keySet());
        Collections.sort(dates);

        LocalDate dateNoMoreCash = null;
        for (LocalDate date : dates) {
            Patrimoine patrimoineJour = evolution.getEvolutionJournaliere().get(date);
            Argent especes = (Argent) patrimoineJour.possessionParNom("Espèces");
            if (especes.getValeurComptable() <= 0) {
                dateNoMoreCash = date;
                break;
            }
        }
        assertEquals(LocalDate.of(2025, 1, 1), dateNoMoreCash);
    }

    @Test
    void zety_part_le_14_fev_2025(){
        ZetyEtudieEn2023 zety = new ZetyEtudieEn2023();
        Patrimoine patrimoine = zety.get();

        var evolutionPatrimoine = new EvolutionPatrimoine(
                "Zety",
                patrimoine,
                LocalDate.of(2023, 7, 3),
                LocalDate.of(2025, 2, 14)
        );
        var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();
        assertEquals(-1528686, evolutionJournaliere.get(LocalDate.of(2025, 2, 14)).getValeurComptable());

    }
}