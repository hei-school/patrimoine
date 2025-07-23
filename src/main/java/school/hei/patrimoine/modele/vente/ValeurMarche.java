package school.hei.patrimoine.modele.vente;

import school.hei.patrimoine.modele.Argent;

import java.time.LocalDate;

public record ValeurMarche(LocalDate t, Argent valeur) {}