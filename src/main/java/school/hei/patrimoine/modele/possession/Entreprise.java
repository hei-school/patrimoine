package school.hei.patrimoine.modele.possession;

import lombok.Getter;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.vendre.Vendable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;
import static school.hei.patrimoine.modele.possession.TypeAgregat.ENTREPRISE;

@Getter
public final class Entreprise extends Possession implements Vendable {

    private Argent valeurMarche;
    private final double tauxEvolutionMarcheAnnuelle;
    private final Map<LocalDate, Argent> historiqueValeurMarche = new HashMap<>();

    private boolean estVendue = false;
    private LocalDate dateVente;
    private Argent valeurVente;
    protected Argent prixVente;

    public Entreprise(
            String nom,
            LocalDate t,
            Argent valeurComptable,
            Argent valeurMarche,
            double tauxEvolutionMarcheAnnuelle
    ) {
        super(nom, t, valeurComptable);
        this.valeurMarche = (valeurMarche != null) ? valeurMarche : valeurComptable;
        this.tauxEvolutionMarcheAnnuelle = tauxEvolutionMarcheAnnuelle;
        historiqueValeurMarche.put(t, this.valeurMarche);
    }


    @Override
    public Entreprise projectionFuture(LocalDate tFutur) {

        if (tFutur.isBefore(t)) {
            Entreprise e = new Entreprise(
                    nom,
                    tFutur,
                    valeurComptable,
                    new Argent(0, devise()),
                    tauxEvolutionMarcheAnnuelle
            );
            e.historiqueValeurMarche.putAll(historiqueValeurMarche);
            return e;
        }

        if (estVendue && !tFutur.isBefore(dateVente)) {
            // Après la vente, la valeur de marché reste constante
            Entreprise vendue = new Entreprise(
                    nom,
                    tFutur,
                    valeurComptable,
                    valeurVente,
                    tauxEvolutionMarcheAnnuelle
            );
            vendue.estVendue = true;
            vendue.dateVente = dateVente;
            vendue.valeurVente = valeurVente;
            vendue.historiqueValeurMarche.putAll(historiqueValeurMarche);
            vendue.historiqueValeurMarche.put(tFutur, valeurVente);
            return vendue;
        }

        long joursEcoules = DAYS.between(t, tFutur);
        var valeurMarcheAjouteeJournaliere = valeurMarche.mult(tauxEvolutionMarcheAnnuelle / 365.);
        var nouvelleValeurMarche = valeurMarche.add(valeurMarcheAjouteeJournaliere.mult(joursEcoules), tFutur);
        Argent valeurFinale = nouvelleValeurMarche.lt(0) ? new Argent(0, devise()) : nouvelleValeurMarche;

        Entreprise eFuture = new Entreprise(
                nom,
                tFutur,
                valeurComptable,
                valeurFinale,
                tauxEvolutionMarcheAnnuelle
        );
        eFuture.historiqueValeurMarche.putAll(historiqueValeurMarche);
        eFuture.historiqueValeurMarche.put(tFutur, valeurFinale);

        if (estVendue && tFutur.isBefore(dateVente)) {
            eFuture.estVendue = true;
            eFuture.dateVente = dateVente;
            eFuture.valeurVente = valeurVente;
        }

        return eFuture;
    }

    @Override
    public void vendre(LocalDate date, Argent prix, Compte compteBeneficiaire) {
        this.dateVente = date;
        this.prixVente = prix;

        // Correction comptable : valeur devient 0
        new FluxArgent("Correction valeur comptable de " + nom,
                getCompteCorrection().getCompte(),
                date, valeurComptable.negate());

        // Flux d'entrée pour le bénéficiaire
        new FluxArgent("Vente de " + nom, compteBeneficiaire, date, prix);
    }

    @Override
    public boolean estVendue() {
        return dateVente != null;
    }

    @Override
    public boolean estActiveALaDate(LocalDate date) {
        return dateVente == null || date.isBefore(dateVente);
    }

    @Override
    public LocalDate getDateVente() {
        return dateVente;
    }

    @Override
    public Argent getPrixVente() {
        return prixVente;
    }

    @Override
    public TypeAgregat typeAgregat() {
        return ENTREPRISE;
    }
}
