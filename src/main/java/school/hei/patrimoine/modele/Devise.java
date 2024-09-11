package school.hei.patrimoine.modele;

import static java.lang.Math.max;
import static java.time.Month.JULY;
import static java.time.temporal.ChronoUnit.DAYS;

import java.io.Serializable;
import java.time.LocalDate;

public record Devise(
    String nom, String symbole, double valeurEnAriary, LocalDate t, double tauxDappréciationAnnuel)
    implements Serializable {
  public static final Devise MGA = new Devise("ARIARY", "Ar", 1, LocalDate.MIN, 0.0);
  public static final Devise EUR =
      new Devise("EURO", "€", 4_821, LocalDate.of(2024, JULY, 3), -0.1);
  public static final Devise CAD =
      new Devise("CAD", "CAD", 3286, LocalDate.of(2024, JULY, 8), -0.1);

  public double valeurEnAriary(LocalDate now) {
    var joursEcoules = DAYS.between(t, now);
    double valeurAjouteeJournaliere = valeurEnAriary * (tauxDappréciationAnnuel / 365.);
    return max(0, (int) (valeurEnAriary + valeurAjouteeJournaliere * joursEcoules));
  }
}
