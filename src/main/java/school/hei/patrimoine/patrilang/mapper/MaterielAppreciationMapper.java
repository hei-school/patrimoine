package school.hei.patrimoine.patrilang.mapper;

import static school.hei.patrimoine.patrilang.modele.MaterielAppreciationType.APPRECIATION;
import static school.hei.patrimoine.patrilang.modele.MaterielAppreciationType.DEPRECIATION;

import school.hei.patrimoine.patrilang.modele.MaterielAppreciationType;

public class MaterielAppreciationMapper {
  public static MaterielAppreciationType stringToMaterielAppreciationType(String token) {
    return switch (token) {
      case "se dépréciant" -> DEPRECIATION;
      case "s'appréciant" -> APPRECIATION;
      default -> throw new IllegalArgumentException("Type d'appréciation inconnu: " + token);
    };
  }
}
