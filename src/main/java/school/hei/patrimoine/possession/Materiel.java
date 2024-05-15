    package school.hei.patrimoine.possession;

    import school.hei.patrimoine.NotImplemented;

    import java.time.Instant;
    import java.time.temporal.ChronoUnit;

    public final class Materiel extends Possession {
      private final double tauxDAppreciationAnnuelle;


      public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
        super(nom, t, valeurComptable);
        this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
      }

      @Override
      public int valeurComptableFuture(Instant tFutur) {
          if (tFutur.isBefore(this.t)) {
              return this.valeurComptable;
          }
          long joursEntre = ChronoUnit.DAYS.between(this.t, tFutur);
          double tauxAmortissementQuotidien = Math.pow(1 - this.tauxDAppreciationAnnuelle, 1.0 / 365);

          double valeurFuture = this.valeurComptable * Math.pow(tauxAmortissementQuotidien, joursEntre);
          return (int) Math.round(valeurFuture);
      }
    }
