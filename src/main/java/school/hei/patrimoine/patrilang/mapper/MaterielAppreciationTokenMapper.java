package school.hei.patrimoine.patrilang.mapper;

import school.hei.patrimoine.patrilang.modele.MaterielAppreciationType;

import static school.hei.patrimoine.patrilang.modele.MaterielAppreciationType.APPRECIATION;
import static school.hei.patrimoine.patrilang.modele.MaterielAppreciationType.DEPRECIATION;

public class MaterielAppreciationTokenMapper {
    public static MaterielAppreciationType stringToMaterielAppreciationType(String token) {
        return switch (token) {
            case "se dépréciant" -> DEPRECIATION;
            case "s'appréciant" -> APPRECIATION;
            default -> throw new IllegalArgumentException("Type d'appréciation inconnu: " + token);
        };
    }
}
