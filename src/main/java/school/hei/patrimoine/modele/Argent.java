package school.hei.patrimoine.modele;

import static school.hei.patrimoine.modele.Devise.EUR;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.io.Serializable;
import java.time.LocalDate;

public record Argent(int montant, Devise devise) implements Serializable {

  public static Argent euro(int montant) {
    return new Argent(montant, EUR);
  }

  public static Argent ariary(int montant) {
    return new Argent(montant, MGA);
  }

  public Argent convertir(Devise autreDevise, LocalDate t) {
    return new Argent(
        (int) ((montant * devise.valeurEnAriary(t)) / autreDevise.valeurEnAriary(t)), autreDevise);
  }

  public Argent mult(double d) {
    return new Argent((int) (montant * d), devise);
  }

  public Argent minus(Argent that, LocalDate t) {
    return new Argent(montant - that.convertir(devise, t).montant, devise);
  }

  public Argent add(Argent that, LocalDate t) {
    return new Argent(montant + that.convertir(devise, t).montant, devise);
  }
}
