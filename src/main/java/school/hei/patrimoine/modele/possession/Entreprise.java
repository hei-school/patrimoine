package school.hei.patrimoine.modele.possession;


import lombok.Getter;
import school.hei.patrimoine.modele.Argent;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import static java.time.temporal.ChronoUnit.DAYS;
import static school.hei.patrimoine.modele.possession.TypeAgregat.ENTREPRISE;
@Getter
public final class Entreprise extends Possession{
    private Argent valeurMarche;
    private final double tauxEvolutionMarcheAnnuelle;
    private final Map<LocalDate, Argent> historiqueValeurMarche = new HashMap<>();

    public Entreprise(
        String nom,
        LocalDate t,
        Argent valeurComptable,
        Argent valeurMarche,
        double tauxEvolutionMarcheAnnuelle) {
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
                    new Argent(0, valeurComptable.devise()),
                    tauxEvolutionMarcheAnnuelle);
            e.historiqueValeurMarche.putAll(historiqueValeurMarche);
            return e;
        }

        long joursEcoules = DAYS.between(t, tFutur);

        var valeurMarcheAjouteeJournaliere = valeurMarche.mult(tauxEvolutionMarcheAnnuelle / 365.);
        var nouvelleValeurMarche =
                valeurMarche.add(valeurMarcheAjouteeJournaliere.mult(joursEcoules), tFutur);

        Argent valeurFinale =
                nouvelleValeurMarche.lt(0) ? new Argent(0, devise()) : nouvelleValeurMarche;

        Entreprise eFuture = new Entreprise(
                nom,
                tFutur,
                valeurComptable, // Valeur comptable reste figée
                valeurFinale,
                tauxEvolutionMarcheAnnuelle);

        // ✅ On garde l’historique précédent et on ajoute la nouvelle date
        eFuture.historiqueValeurMarche.putAll(historiqueValeurMarche);
        eFuture.historiqueValeurMarche.put(tFutur, valeurFinale);

        return eFuture;
    }

    @Override
    public TypeAgregat typeAgregat() {
        return ENTREPRISE;
    }
}
