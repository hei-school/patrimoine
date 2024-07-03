package school.hei.patrimoine.modele.possession;

import java.util.HashMap;
import java.util.Map;

public class TauxDeChange {
    private final Map<String, Double> tauxDeChange = new HashMap<>();

    public void ajouterTauxDeChange(String devise, double taux) {
        tauxDeChange.put(devise, taux);
    }

    public double obtenirTauxDeChange(String devise) {
        return tauxDeChange.getOrDefault(devise, 1.0);
    }
}