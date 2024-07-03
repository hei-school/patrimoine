package school.hei.patrimoine.modele.currency;

import java.util.HashSet;
import java.util.Set;

public class Devise{
    private final String nom;
    private final String code;
    private final Set<TauxDeChange> tauxDeChange;

    public Devise(String nom, String code) {
        this.nom = nom;
        this.code = code;
        this.tauxDeChange = new HashSet<>();
    }

    public int convertir(int valeur, Devise devise){
        var tauxPourDevise = prendreTauxDeChangePour(devise);
        return (int) (valeur * tauxPourDevise.getValeur());
    }

    private TauxDeChange prendreTauxDeChangePour(Devise devise){
        return tauxDeChange.stream()
                .filter(taux -> taux.getDeviseDestination().equals(devise))
                .toList().getFirst();
    }

    void addTauxDeChange(TauxDeChange tauxDeChange){
        if(!this.tauxDeChange.contains(tauxDeChange))
            this.tauxDeChange.add(tauxDeChange);
    }
}
