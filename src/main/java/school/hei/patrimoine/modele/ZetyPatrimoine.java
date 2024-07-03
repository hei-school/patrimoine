package school.hei.patrimoine.modele;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
public class ZetyPatrimoine {
    private static final double APPRECIATION_ORDINATEUR = -0.10;
    private static final double APPRECIATION_VETEMENTS = -0.50;
    private static final double DON_MENSUEL = 100000;
    private static final LocalDate FIN_DEPENSES =LocalDate.of(2025,15,02);
    private  double DEPENSES_MENSUELLES ;

    private double ordinateurValeur;
    private double vetementsValeur;
    private double argentEspeces;
    private double compteBancaire;
    private final double fraisScolariteMensuels;
    private final double fraisTenueCompteMensuels;
    private double dette = 0;

    public ZetyPatrimoine() {
        this.ordinateurValeur = 1_200_000;
        this.vetementsValeur = 1_500_000;
        this.argentEspeces = 800_000;
        this.compteBancaire = 100_000;
        this.fraisScolariteMensuels = 200_000;
        this.fraisTenueCompteMensuels = 20_000;
    }

    public double calculerValeurPatrimoine(LocalDate dateInitiale, LocalDate dateFinale) {
        long joursEntreDates = ChronoUnit.DAYS.between(dateInitiale, dateFinale);

        double valeurOrdinateur = ordinateurValeur * Math.pow(1 + APPRECIATION_ORDINATEUR / 365, joursEntreDates);
        double valeurVetements = vetementsValeur * Math.pow(1 + APPRECIATION_VETEMENTS / 365, joursEntreDates);

        long moisScolarite = ChronoUnit.MONTHS.between(LocalDate.of(2023, 11, 27), dateFinale);
        double totalFraisScolarite = Math.min(moisScolarite, 10) * fraisScolariteMensuels;

        long moisTenueCompte = ChronoUnit.MONTHS.between(dateInitiale.withDayOfMonth(25), dateFinale.withDayOfMonth(25));
        double totalFraisTenueCompte = moisTenueCompte * fraisTenueCompteMensuels;

        return valeurOrdinateur + valeurVetements + argentEspeces - totalFraisScolarite + compteBancaire - totalFraisTenueCompte;
    }
    public void sEndetter(double montant, LocalDate date) {
        compteBancaire += montant;
        argentEspeces -= montant;
    }

    public void payerFraisScolarite(double montant, LocalDate date) {
        compteBancaire -= montant;
    }

    public void recevoirDonParents(LocalDate date) {
        argentEspeces += 100_000;
    }

    public void payerTrainVieMensuel(LocalDate date) {
        argentEspeces -= 250_000;
    }
    public void contracterDette(LocalDate date, double montantPret, double montantDette) {
        if (date.equals(LocalDate.of(2024, 9, 18))) {
            compteBancaire += montantPret;
            dette += montantDette;
        }
    }

    public void payerFraisScolarite(LocalDate date, double montant) {
        if (date.equals(LocalDate.of(2024, 9, 21))) {
            compteBancaire -= montant;
        }
    }

    public void recevoirDonMensuel(LocalDate date) {
        if (date.getDayOfMonth() == 15 && !date.isBefore(LocalDate.of(2024, 1, 15))) {
            argentEspeces += DON_MENSUEL;
        }
    }

    public void payerDepensesMensuelles(LocalDate date) {
        if (date.getDayOfMonth() == 1 && date.isAfter(LocalDate.of(2024, 9, 30)) && !date.isAfter(FIN_DEPENSES)) {
            argentEspeces -= DEPENSES_MENSUELLES;
        }
    }

    public LocalDate dateEpuisementEspeces() {
        LocalDate currentDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = FIN_DEPENSES;
        while (!currentDate.isAfter(endDate)) {
            recevoirDonMensuel(currentDate);
            payerDepensesMensuelles(currentDate);
            if (argentEspeces < 0) {
                return currentDate;
            }
            currentDate = currentDate.plusDays(1);
        }
        return null;
    }


    public double calculerValeurPatrimoineEnDevise(LocalDate date, String devise, double tauxDeChange, double appreciationAnnuelle) {
        double valeurPatrimoine = calculerValeurPatrimoine(LocalDate.of(2024, 7, 3), date);
        long joursDepuisDebut = ChronoUnit.DAYS.between(LocalDate.of(2024, 7, 3), date);
        double tauxChangeApplique = tauxDeChange * Math.pow(1 + appreciationAnnuelle / 365, joursDepuisDebut);
        return valeurPatrimoine / tauxChangeApplique;
    }
}


