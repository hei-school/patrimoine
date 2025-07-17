package school.hei.patrimoine.modele;

import java.io.Serializable;
import java.time.LocalDate;

public record ValeurMarche(LocalDate t, Argent valeurComptable) implements Serializable {}