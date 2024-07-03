package school.hei.patrimoine.modele.possession;

public class DeviseValue {
    private final int valeur;
    private final Devise devise;

    public DeviseValue(int valeur, Devise devise) {
        this.valeur = valeur;
        this.devise = devise;
    }

    public int value() {
        return valeur;
    }

    public int getValeurEuro() {
        if (devise == Devise.EURO) return valeur;
        return valeur / 4_821;
    }

    public int getValeurAriary() {
        if (devise == Devise.ARIARY) return valeur;
        return valeur * 4_821;
    }
}
