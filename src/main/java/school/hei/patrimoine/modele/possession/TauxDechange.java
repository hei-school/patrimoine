package school.hei.patrimoine.modele.possession;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class TauxDechange {
    private final double valeur;
    private final Devise deviseUn;
    private final Devise deviseDeux;
    private final double tauxAppreciationAnnuel;
    private final LocalDate t;
}
