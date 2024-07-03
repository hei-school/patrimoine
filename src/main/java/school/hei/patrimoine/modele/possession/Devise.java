package school.hei.patrimoine.modele.possession;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Devise {
    private final String nom ;
    private final double montant;
    private LocalDate date;
    private final double tauxDeChangeEnAriary;

    public double valeurEnAriary (){
        return montant * tauxDeChangeEnAriary;
    }

}
