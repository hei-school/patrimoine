package school.hei.patrimoine.visualisation.swing.ihm.google.modele.fec.mapper;

import static java.time.LocalDate.now;
import static school.hei.patrimoine.modele.Devise.EUR;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Locale;
import school.hei.patrimoine.modele.fec.EcritureComptable;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.fec.LigneEcriture;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.fec.FecLine;

public class FecLineMapper {
  public static FecLine toFecLine(
      Journal journal, EcritureComptable ecriture, LigneEcriture ligne) {
    var compte = ligne.compte();
    var compAux = ligne.compteAuxiliaire();
    var pj = ligne.pieceJustificative();
    var flux = ligne.flux();

    var montantEur = flux.convertir(EUR, now());
    var isDebit = montantEur.montant() >= 0;

    return FecLine.builder()
        .journalCode(journal.code().toString())
        .journalLib(journal.libelle())
        .ecritureNum(ecriture.id())
        .ecritureDate(ecriture.date() != null ? formatDate(ecriture.date()) : "")
        .compteNum(compte != null ? compte.nom() : "")
        .compteLib(compte != null ? compte.nom() : "")
        .compAuxNum(compAux != null ? compAux.nom() : "")
        .compAuxLib(compAux != null ? compAux.nom() : "")
        .pieceRef(pj != null ? pj.id() : "")
        .pieceDate(pj != null ? formatDate(pj.date()) : "")
        .ecritureLib(ecriture.libelle())
        .debit(isDebit ? formatAmount(montantEur.montant()) : "")
        .credit(isDebit ? "" : formatAmount(montantEur.montant()))
        .ecritureLet(ligne.lettrage() != null ? ligne.lettrage() : "")
        .dateLet(ligne.dateLettrage() != null ? formatDate(ligne.dateLettrage()) : "")
        .validDate(ecriture.dateValidation() != null ? formatDate(ecriture.dateValidation()) : "")
        .montantDevise(formatAmount(flux.convertir(MGA, LocalDate.now()).montant()))
        .idevise(flux.devise().codeIso())
        .build();
  }
  ;

  private static String formatDate(LocalDate date) {
    return date.toString().replace("-", "");
  }

  private static String formatAmount(double montant) {
    return String.format(Locale.US, "%.2f", Math.abs(montant));
  }
}
