package school.hei.patrimoine.patrilang.mapper;

import school.hei.patrimoine.patrilang.modele.DurationType;

import static school.hei.patrimoine.patrilang.modele.DurationType.*;

public class DurationMapper {
    public static DurationType stringToDurationType(String token) {
        return switch (token){
            case "en années", "en Années","années de"  -> YEARS;
            case "en mois", "en Mois", "mois de" -> MONTH;
            case "en jours", "en Jours", "jours  de"-> DAYS;
            default -> throw new IllegalArgumentException("Type de duration inconnue : " + token);
        };
    }
}
