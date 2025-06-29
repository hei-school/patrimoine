package school.hei.patrimoine.patrilang.mapper;

import java.time.Month;

import static java.time.Month.*;

public class MonthTokenMapper {
    public static Month stringToMonth(String token) {
        return switch (token) {
            case "Janvier", "janvier" -> JANUARY;
            case "Février", "février", "Fevrier", "fevrier" -> FEBRUARY;
            case "Mars", "mars" -> MARCH;
            case "Avril", "avril" -> APRIL;
            case "Mai", "mai" -> MAY;
            case "Juin", "juin" -> JUNE;
            case "Juillet", "juillet" -> JULY;
            case "Août", "août", "Aout", "aout" -> AUGUST;
            case "Septembre", "septembre" -> SEPTEMBER;
            case "Octobre", "octobre" -> OCTOBER;
            case "Novembre", "novembre" -> NOVEMBER;
            case "Décembre", "décembre", "Decembre", "decembre" -> DECEMBER;
            default -> throw new IllegalArgumentException("Mois invalide : " + token);
        };
    }
}
