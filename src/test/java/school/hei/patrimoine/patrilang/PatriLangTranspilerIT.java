package school.hei.patrimoine.patrilang;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.APRIL;
import static org.antlr.v4.runtime.CharStreams.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

public class PatriLangTranspilerIT {
    PatriLangTranspiler subject = new PatriLangTranspiler();
    private static final LocalDate AU_18_AVRIL_2025 = LocalDate.of(2025, APRIL, 18);

    @Test
    void patrimoine_without_possession_ok(){
        var expected = patrimoineWithoutPossessions();
        var input = fromString(SECTION_GENERAL);

        var actual = subject.apply(input);

        assertEquals(expected.getT(), actual.getT());
        assertEquals(expected.getNom(), actual.getNom());
        assertEquals(expected.getDevise(), actual.getDevise());
        assertEquals(expected.getValeurComptable(), actual.getValeurComptable());
    }

    @Test
    void patrimoine_with_trésorier_ok(){
        var expected = patrimoineWithTrésoriers();
        var input = fromString(SECTION_GENERAL + SECTION_TRESORIER);

        var actual = subject.apply(input);

        assertEquals(expected.getT(), actual.getT());
        assertEquals(expected.getNom(), actual.getNom());
        assertEquals(expected.getDevise(), actual.getDevise());
        assertEquals(expected.getValeurComptable(), actual.getValeurComptable());
        assertEquals(expected.getPossessions(), actual.getPossessions());
    }

    @Test
    void patrimoine_with_créance_ok(){
        var expected = patrimoineWithCréances();
        var input = fromString(SECTION_GENERAL + SECTION_CREANCE);

        var actual = subject.apply(input);

        assertEquals(expected.getT(), actual.getT());
        assertEquals(expected.getNom(), actual.getNom());
        assertEquals(expected.getDevise(), actual.getDevise());
        assertEquals(expected.getValeurComptable(), actual.getValeurComptable());
        assertEquals(expected.getPossessions(), actual.getPossessions());
    }

    Patrimoine patrimoineWithoutPossessions(){
        return Patrimoine.of(
            "Patrimoine de Zety",
            MGA,
            AU_18_AVRIL_2025,
            new Personne("Zety"),
            Set.of()
        );
    }

    Patrimoine patrimoineWithTrésoriers(){
        return Patrimoine.of(
            "Patrimoine de Zety",
            MGA,
            AU_18_AVRIL_2025,
            new Personne("Zety"),
            trésoriers()
        );
    }

    Patrimoine patrimoineWithCréances(){
        return Patrimoine.of(
            "Patrimoine de Zety",
            MGA,
            AU_18_AVRIL_2025,
            new Personne("Zety"),
            créances()
        );
    }


    Set<Possession> trésoriers(){
        return Set.of(
            new Compte("BMOI", AU_18_AVRIL_2025, ariary(15_000)),
            new Compte("BNI", AU_18_AVRIL_2025, ariary(15_000))
        );
    }

    Set<Possession> créances(){
        return Set.of(
            new Creance("Myriade_Fr", AU_18_AVRIL_2025, ariary(5_000)),
            new Creance("FanoCréance", AU_18_AVRIL_2025, ariary(3_000))
        );
    }

    private final static String SECTION_GENERAL = """
        # Général
        * Spécifié le 18 du 04-2025
        * Patrimoine de Zety
        * Devise en Ar
    """;

    private final static String SECTION_TRESORIER = """
        # Trésoreries
        * BMOI contient 15000Ar le 18 du 04-2025
        * BNI contient 15000Ar le 18 du 04-2025
    """;

    private final static String SECTION_CREANCE = """
        # Créances
        * Myriade_Fr, valant 5000Ar le 18 du 04-2025
        * FanoCréance, valant 3000Ar le 18 du 04-2025
    """;
}
