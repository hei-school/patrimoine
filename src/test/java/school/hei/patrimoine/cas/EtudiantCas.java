package school.hei.patrimoine.cas;

import static java.time.Month.DECEMBER;
import static java.time.Month.SEPTEMBER;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class EtudiantCas extends Cas {

  private final Compte financeur;
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final Personne personne;

  public EtudiantCas(int startYear, int endYear, Personne personne, int objectif) {
    super(LocalDate.of(startYear, SEPTEMBER, 17), LocalDate.of(endYear, DECEMBER, 31), personne);
    this.startDate = LocalDate.of(startYear, SEPTEMBER, 17);
    this.endDate = LocalDate.of(endYear, DECEMBER, 31);
    this.personne = personne;
    this.financeur = new Compte("Espèces", LocalDate.now(), ariary(objectif));
    ;
  }

  @Override
  protected String nom() {
    return personne.nom();
  }

  @Override
  protected Devise devise() {
    return MGA;
  }

  @Override
  protected void init() {
    new FluxArgent("Init compte Espèces", financeur, startDate, ariary(700_000));
  }

  @Override
  public Set<Possession> possessions() {
    var trainDeVie = new FluxArgent("Vie courante", financeur, startDate, endDate, 15, ariary(0));
    var mac = new Materiel("MacBook Pro", startDate.minusDays(3), startDate, ariary(500_000), 0);
    return Set.of(financeur, trainDeVie, mac);
  }

  @Override
  protected void suivi() {
    new Correction(new FluxArgent("Correction à la hausse", financeur, LocalDate.now(), ariary(0)));
  }
}
