package school.hei.patrimoine.modele.vente;

import school.hei.patrimoine.modele.Argent;
import java.time.LocalDate;

public record ValeurMarche(LocalDate date, Argent valeur) /*note(no-serializable)*/ {
    public ValeurMarche {
        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas être null");
        }
        if (valeur == null) {
            throw new IllegalArgumentException("La valeur ne peut pas être null");
        }
    }
}