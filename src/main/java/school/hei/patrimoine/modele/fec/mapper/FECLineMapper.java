package school.hei.patrimoine.modele.fec.mapper;

import static java.time.LocalDate.now;
import static school.hei.patrimoine.modele.Devise.EUR;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import school.hei.patrimoine.modele.fec.EcritureComptable;
import school.hei.patrimoine.modele.fec.FECLine;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.fec.LigneEcriture;

public class FECLineMapper {
  public static FECLine toFECLine(
      Journal journal, EcritureComptable ecriture, LigneEcriture ligne) {
    var compte = ligne.compte();
    var compAux = ligne.compteAuxiliaire();
    var pj = ecriture.pj();
    var flux = ligne.flux();

    var montantEur = flux.convertir(EUR, now());
    var isDebit = montantEur.montant() >= 0;

    return new FECLine(
        List.of(
            journal.code().toString(),
            journal.libelle(),
            ecriture.id(),
            ecriture.date() != null ? formatDate(ecriture.date()) : "",
            compte.typeComptable().toString(),
            compte.compte().nom(),
            compAux != null ? compAux.typeComptable().toString() : "",
            compAux != null ? compAux.compte().nom() : "",
            pj != null ? pj.id() : "",
            pj != null ? formatDate(pj.date()) : "",
            ecriture.libelle(),
            isDebit ? formatAmount(montantEur.montant()) : "",
            isDebit ? "" : formatAmount(montantEur.montant()),
            ligne.lettrage() != null ? ligne.lettrage() : "",
            ligne.dateLettrage() != null ? formatDate(ligne.dateLettrage()) : "",
            ecriture.dateValidation() != null ? formatDate(ecriture.dateValidation()) : "",
            formatAmount(flux.convertir(MGA, LocalDate.now()).montant()),
            MGA.codeIso()));
  }
  ;

  private static String formatDate(LocalDate date) {
    return date.toString().replace("-", "");
  }

  private static String formatAmount(double montant) {
    return String.format(Locale.US, "%.2f", Math.abs(montant));
  }
}
