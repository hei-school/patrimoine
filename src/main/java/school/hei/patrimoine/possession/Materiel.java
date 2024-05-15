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
        long jourEntre = ChronoUnit.YEARS.between(this.t, tFutur);
        double futureValue = this.valeurComptable * Math.pow(1 + tauxDAppreciationAnnuelle , jourEntre);
        return (int) futureValue;
      }
    }
