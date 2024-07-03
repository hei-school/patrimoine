package school.hei.patrimoine.modele.possession;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Devise {
    private  String code;
    private  double tauxChange;
    private  double tauxAppreciationAnnuel;
}
