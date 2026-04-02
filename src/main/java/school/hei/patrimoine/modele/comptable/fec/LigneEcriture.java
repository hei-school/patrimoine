package school.hei.patrimoine.modele.comptable.fec;

import java.time.LocalDate;
import lombok.Builder;
import school.hei.patrimoine.modele.comptable.CompteComptable;

@Builder
public record LigneEcriture(
    CompteComptable compteComptable,
    CompteComptable compteAuxiliaire,
    String lettrage,
    LocalDate dateLettrage) {}
