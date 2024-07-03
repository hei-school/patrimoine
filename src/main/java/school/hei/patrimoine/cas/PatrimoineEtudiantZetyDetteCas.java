package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;

public class PatrimoineEtudiantZetyDetteCas implements Supplier<Patrimoine> {

  private final LocalDate dateDebut = LocalDate.of(2024, 7, 3);
  private final LocalDate dateFin = LocalDate.of(2025, 9, 18);

  @Override
  public Patrimoine get() {
    var zety = new Personne("Zety");

    var ordinateur = new Materiel("Ordinateur", dateDebut, 1_200_000, dateDebut, -0.1);
    var vetements = new Materiel("Vêtements", dateDebut, 1_500_000, dateDebut, -0.5);
    var argent = new Argent("Argent liquide", dateDebut, 800_000);
    var compteBancaire = new Argent("Compte bancaire", dateDebut, 100_000);
    var dette = new Dette("Dette bancaire", dateDebut, 0);

    new FluxArgent("Frais de scolarité 2023-2024", argent, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);

    new FluxArgent("Frais bancaires", compteBancaire, dateDebut, dateFin, -20_000, 25);

    LocalDate dateEmprunt = LocalDate.of(2024, 9, 18);
    new FluxArgent("Emprunt bancaire", compteBancaire, dateEmprunt, dateEmprunt, 10_000_000, dateEmprunt.getDayOfMonth());
    new FluxArgent("Dette bancaire création", dette, dateEmprunt, dateEmprunt, -11_000_000, dateEmprunt.getDayOfMonth());

    LocalDate dateRemboursement = dateEmprunt.plusYears(1);
    new FluxArgent("Remboursement emprunt", compteBancaire, dateRemboursement, dateRemboursement, -11_000_000, dateRemboursement.getDayOfMonth());
    new FluxArgent("Dette bancaire annulation", dette, dateRemboursement, dateRemboursement, 11_000_000, dateRemboursement.getDayOfMonth());

    return new Patrimoine("Zety s'endette", zety, dateDebut, Set.of(ordinateur, vetements, argent, compteBancaire, dette));
  }
}