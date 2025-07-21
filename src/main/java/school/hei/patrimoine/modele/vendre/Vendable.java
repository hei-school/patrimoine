package school.hei.patrimoine.modele.vendre;

import school.hei.patrimoine.modele.Argent;

import java.time.LocalDate;

public interface Vendable {
    void vendre(LocalDate date, Argent prix);

    boolean estVendue();

    boolean estActiveALaDate(LocalDate date);

    LocalDate getDateVente();

    Argent getPrixVente();
}
