package school.hei.patrimoine.modele;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.util.HashMap;
import lombok.Getter;

@Getter
public class Devise {
  private final String name;
  private final HashMap<Devise, TauxDeChange> tauxDeChanges = new HashMap<>();

  public double from(
      double valeur, Devise devise, LocalDate date, double tauxDAppreciationAnnuelle) {
    TauxDeChange tauxDeChange = tauxDeChanges.get(devise);

    var joursEcoules = DAYS.between(tauxDeChange.date(), date);
    double valeurAjouteeJournaliere = tauxDeChange.valeur() * (tauxDAppreciationAnnuelle / 365.);
    double valeurFuture = (tauxDeChange.valeur() + valeurAjouteeJournaliere * joursEcoules);
    return valeur / valeurFuture;
  }

  public Devise(String name) {
    this.name = name;
  }

  public void addTauxDeChange(Devise devise, TauxDeChange tauxDeChange) {
    tauxDeChanges.put(devise, tauxDeChange);
  }
}
