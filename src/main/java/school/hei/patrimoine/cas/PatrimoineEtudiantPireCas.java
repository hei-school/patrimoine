package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Monnaie;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;

import static java.time.Month.MAY;

public class PatrimoineEtudiantPireCas implements Supplier<Patrimoine> {
  @Override
  public Patrimoine get() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var arriary = new Monnaie("arriary", 4_821, au13mai24, -.1);
    var financeur = new Argent("Espèces", au13mai24.minusDays(1), au13mai24, 400_000, arriary);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        financeur,
        au13mai24.minusDays(100),
        au13mai24.plusDays(100),
        -100_000,
        15, arriary);

    var mac = new Materiel(
        "MacBook Pro",
        au13mai24,
        500_000,
        au13mai24.minusDays(3),
        -0.9, arriary);

    return new Patrimoine(
        "Ilo (pire)",
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie, mac));
  }
}
