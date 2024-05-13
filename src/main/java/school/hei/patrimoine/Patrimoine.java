package school.hei.patrimoine;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class Patrimoine {
    private final Personne possesseur;
    private Instant t;
    private Set<Possession> possessions;
    private int valeurComptable;
    private int valeurComptableActuelle;

    public Patrimoine(Personne possesseur, Instant parse) {
        this.possesseur = possesseur;
        this.possessions = new HashSet<>();
        this.t = t;
    }

    public int getValeurComptable(Instant t) {
        return valeurComptable;
    }

    public int getValeurComptableActuelle() {
        return valeurComptableActuelle;
    }

    public void addPossession(Possession possession) {
        possessions = possession
    }
}
