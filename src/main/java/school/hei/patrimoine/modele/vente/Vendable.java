package school.hei.patrimoine.modele.vente;

import school.hei.patrimoine.modele.Argent;

import java.time.LocalDate;

public interface Vendable {
    public abstract Argent getValeurMarche(LocalDate t);
}
