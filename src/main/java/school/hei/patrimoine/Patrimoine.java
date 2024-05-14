package school.hei.patrimoine;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class Patrimoine {
    private final Personne possesseur;
    private final Instant t;
    //pour definir une possesion a un instant t pour ne plus y toucher on les mets en prive
    private final Set<Possession> possessions;
   //on supprime et on le met aec possession private int valeurComptable;

    public Patrimoine(Personne possesseur, Instant t) {
        this.possesseur = possesseur;
        this.possessions = new HashSet<>();
        this.t = t;
    }

    public int getValeurComptable(Instant t) {
        if(possessions.isEmpty()) {
            return 0;
        }
        throw new  RuntimeException("TODO");
    }


    public void addPossession(Possession possession) {
        possessions.add(possession);
    }
}
