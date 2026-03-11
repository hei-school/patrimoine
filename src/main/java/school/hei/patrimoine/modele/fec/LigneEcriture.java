package school.hei.patrimoine.modele.fec;

import java.time.LocalDate;
import lombok.Builder;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.CompteComptable;

@Builder
public record LigneEcriture(
    CompteComptable compte,
    CompteComptable compteAuxiliaire,
    Argent flux,
    String lettrage,
    LocalDate dateLettrage) {}
