package school.hei.patrimoine.modele.vente;

import java.io.Serializable;
import java.time.LocalDate;

import school.hei.patrimoine.modele.Argent;

public record ValeurMarche(LocalDate t, Argent valeur) implements Serializable {}
