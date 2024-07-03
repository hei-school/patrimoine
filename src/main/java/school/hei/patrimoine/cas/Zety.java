package school.hei.patrimoine.cas;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Zety {
    private Map<String, Integer> possessions;

    public Zety(){
        this.possessions = new HashMap<>();
    }
    public void ajouterDette(String nom, LocalDate date, int valeur) {
        possessions.put(nom, possessions.getOrDefault(nom, 0) + valeur);
    }

    public int getValeurTotale(LocalDate date) {
        return possessions.values().stream().mapToInt(Integer::intValue).sum();
    }

}
