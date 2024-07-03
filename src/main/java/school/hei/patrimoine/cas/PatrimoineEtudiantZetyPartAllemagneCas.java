package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;
import school.hei.patrimoine.modele.Devise;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;

public class PatrimoineEtudiantZetyCasAriary implements Supplier<Patrimoine> {

    private final LocalDate ajd = LocalDate.of(2024, 7, 3);
    private final LocalDate finSimulation = LocalDate.of(2025, 10, 26);

    @Override
    public Patrimoine get() {
        var zety = new Personne("Zety");
        
        var ordinateur = new Materiel("Ordinateur", ajd, 1_200_000, ajd, -0.1, Devise.AR);
        var vetements = new Materiel("Vêtements", ajd, 1_500_000, ajd, -0.5, Devise.AR);
        var argent = new Argent("Argent liquide", ajd, 800_000, Devise.AR);
        var compteBancaire = new Argent("Compte bancaire", ajd, 100_000, Devise.AR);
        var dette = new Dette("Dette bancaire", ajd, 0, Devise.AR);

        new FluxArgent("Frais de scolarité 2023-2024", argent, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27, Devise.AR);

        new FluxArgent("Frais bancaires", compteBancaire, ajd, finSimulation, -20_000, 25, Devise.AR);

        LocalDate dateEmprunt = LocalDate.of(2024, 9, 18);
        new FluxArgent("Emprunt bancaire", compteBancaire, dateEmprunt, dateEmprunt, 10_000_000, dateEmprunt.getDayOfMonth(), Devise.AR);
        new FluxArgent("Dette bancaire création", dette, dateEmprunt, dateEmprunt, -11_000_000, dateEmprunt.getDayOfMonth(), Devise.AR);
        
        LocalDate dateRemboursement = dateEmprunt.plusYears(1);
        new FluxArgent("Remboursement emprunt", compteBancaire, dateRemboursement, dateRemboursement, -11_000_000, dateRemboursement.getDayOfMonth(), Devise.AR);
        new FluxArgent("Dette bancaire annulation", dette, dateRemboursement, dateRemboursement, 11_000_000, dateRemboursement.getDayOfMonth(), Devise.AR);

        var detteDeutscheBank = new Dette("Dette Deutsche Bank", LocalDate.of(2025, 2, 15), -7000, Devise.EUR);

        return new Patrimoine("Zety (étudiant)", zety, ajd, Set.of(ordinateur, vetements, argent, compteBancaire, dette, detteDeutscheBank));
    }
}
