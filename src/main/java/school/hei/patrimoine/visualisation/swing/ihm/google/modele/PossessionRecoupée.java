package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import lombok.Getter;
import school.hei.patrimoine.modele.possession.Possession;

public record PossessionRecoupée(Possession possession, Status status) {
    @Getter
    public enum Status {
        TOUT("Tout"),
        NON_PRÉVU("Non Prévu"),
        NON_ÉXECUTÉ("Non Éxecuté"),
        ÉXECUTÉ_AVEC_DIFFÉRENCE("Éxecuté avec différence"),
        ÉXECUTÉ_SANS_DIFFÉRENCE("Éxecuté sans différence");

        public final String label;

        Status(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
