package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

public enum PossessionRecoupeeFilterPj {
    TOUT(""),
    AVEC_PJ("Avec PJ"),
    SANS_PJ("Sans PJ");

    public final String label;

    PossessionRecoupeeFilterPj(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
