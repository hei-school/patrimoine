package school.hei.patrimoine.patrilang.generator;

import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.PersonneMorale;

import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;

public class VariableTypePatriLangGenerator implements PatriLangGenerator<Object>{
    @Override
    public String apply(Object data) {
        return switch (data){
            case Creance ignored -> CREANCE.getValue();
            case Dette ignored -> DETTE.getValue();
            case Compte ignored -> TRESORERIES.getValue();
            case Personne ignored -> PERSONNE.getValue();
            case PersonneMorale ignored -> PERSONNE_MORALE.getValue();
            default -> throw new IllegalArgumentException("Not Supported Yet");
        };
    }
}
