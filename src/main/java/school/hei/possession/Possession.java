package school.hei.possession;

import java.time.Instant;

public sealed class Possession permits
        Argent, Materiel, TrainDeVie {
    protected final String nom;
    protected final Instant t;
    protected final int valeurComptable;

    public Possession(String nom, Instant t, int valeurComptable) {
        this.nom = nom;
        this.t = t;
        this.valeurComptable = valeurComptable;
    }
}
