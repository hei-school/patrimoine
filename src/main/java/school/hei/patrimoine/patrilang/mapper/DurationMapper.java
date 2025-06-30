package school.hei.patrimoine.patrilang.mapper;

import school.hei.patrimoine.patrilang.modele.DurationType;

import static school.hei.patrimoine.patrilang.modele.DurationType.*;

public class DurationMapper {
    public static DurationType stringToDurationType(String token) {
        return switch (token){
            case "en années", "en Années" -> YEARS;
            case "en mois", "en Mois" -> MONTH;
            case "en jours", "en Jours" -> DAYS;
            default -> throw new IllegalArgumentException("Type de duration inconnue : " + token);
        };
    }
}
