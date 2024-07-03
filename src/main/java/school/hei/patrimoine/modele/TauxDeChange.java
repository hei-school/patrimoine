package school.hei.patrimoine.modele;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;



import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class TauxDeChange {
    private final double valeur;
    private final Devise deviseUn;
    private final Devise deviseDeux;
    private final double tauxAppreciationAnnuel;
    private final LocalDate t;
}