package school.hei.patrimoine.modele.devise;

import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

import static java.lang.Math.max;
import static java.time.temporal.ChronoUnit.DAYS;

@Getter
public class Devise {
    private final String name;
    private final HashMap<Devise, TauxDeChange> tauxDeChanges = new HashMap<>();

    public double from(double valeur, Devise devise, LocalDate date, double tauxDAppreciationAnnuelle) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Devise devise = (Devise) o;
        return Objects.equals(name, devise.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}