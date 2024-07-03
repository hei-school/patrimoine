package school.hei.patrimoine.modele;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import school.hei.patrimoine.modele.possession.Argent;

@Getter
@Setter
@AllArgsConstructor
public class Devise {
    private final Argent valeurDeDepart;
    private final double tauxDeChange;

    public double conversion(){
        return valeurDeDepart.getValeurComptable() * tauxDeChange;
    }
}
