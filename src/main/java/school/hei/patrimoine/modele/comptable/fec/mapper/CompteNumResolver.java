package school.hei.patrimoine.modele.comptable.fec.mapper;

import java.util.LinkedHashMap;
import java.util.Map;
import school.hei.patrimoine.modele.comptable.CompteComptable;

public class CompteNumResolver {

  private final Map<String, String> registry = new LinkedHashMap<>();

  private final Map<Integer, Integer> counters = new LinkedHashMap<>();

  public String resolve(CompteComptable compte) {
    var codePCG = compte.typeComptable().codePCG();
    var nom = compte.compte().nom();
    var key = codePCG + ":" + nom;

    return registry.computeIfAbsent(
        key,
        k -> {
          var seq = counters.merge(codePCG, 1, Integer::sum);
          return String.format("%d%03d", codePCG, seq);
        });
  }
}
