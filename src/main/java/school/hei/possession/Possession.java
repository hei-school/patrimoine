package school.hei.possession;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public sealed class Possession permits Argent, Materiel, TrainDeVie {
    protected final String nom;
    protected final Instant t;
    protected final int valeurComptable;
}
