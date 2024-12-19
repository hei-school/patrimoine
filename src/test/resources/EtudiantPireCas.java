package cas.drive;

import java.time.LocalDate;

public class EtudiantPireCas {

  public String nom;
  public LocalDate date;
  public int devise;

  public EtudiantPireCas() {
    this.nom = "Ilo";
    this.date = LocalDate.of(2000, 1, 1);
    this.devise = 3000;
  }
}
