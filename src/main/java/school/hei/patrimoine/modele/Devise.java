package school.hei.patrimoine.modele;

import static java.lang.Math.max;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;

public record Devise(String nom, int valeurEnAriary, LocalDate t, double tauxDappréciationAnnuel) {
	public static final Devise ARIARY = new Devise("ARIARY", 1, LocalDate.MIN, 0.0);

	public double valeurEnAriary(LocalDate t) {
		var joursEcoules = DAYS.between(t, now());
		double valeurAjouteeJournaliere = valeurEnAriary * (tauxDappréciationAnnuel / 365.);
		return max(0, (int) (valeurEnAriary + valeurAjouteeJournaliere * joursEcoules));
	}
}
