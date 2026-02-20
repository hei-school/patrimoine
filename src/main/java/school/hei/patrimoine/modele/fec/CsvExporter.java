package school.hei.patrimoine.modele.fec;

import static school.hei.patrimoine.modele.decomposeur.PossessionCompteResolver.resolve;
import static school.hei.patrimoine.modele.fec.FECFields.*;
import static school.hei.patrimoine.modele.fec.JournalCode.*;

import com.opencsv.CSVWriter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.swing.*;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.*;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;

public class CsvExporter {
  public static final String CSV_FILE_EXTENSION = ".csv";

  private static final String DOWNLOADS_PATH = "Downloads";
  private static final String TELECHARGEMENTS_PATH = "Téléchargements";

  public static void exportCsv(
      List<PossessionRecoupee> possessions, Set<PieceJustificative> pjs, File selectedFile)
      throws IOException {
    Optional<File> outputFile = getOutputFile(selectedFile);
    if (outputFile.isEmpty()) return;

    try (Writer writer =
            new OutputStreamWriter(new FileOutputStream(outputFile.get()), StandardCharsets.UTF_8);
        var csvWriter =
            new CSVWriter(
                writer,
                ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {

      writer.write('\uFEFF');

      csvWriter.writeNext(getFecHeader());

      int sequence = 0;
      for (PossessionRecoupee p : possessions) {
        if (p.ecartValeurAvecRealises().montant() != 0) continue;

        csvWriter.writeNext(getFecLine(p, pjs, sequence, false));
        csvWriter.writeNext(getFecLine(p, pjs, sequence, true));
        sequence++;
      }
    }
  }

  private static Optional<File> getOutputFile(File selectedFile) {
    var fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Exporter le fichier CSV");
    fileChooser.setCurrentDirectory(getDefaultDir());
    var baseName = selectedFile.getName().split("\\.", 2)[0];
    fileChooser.setSelectedFile(new File(baseName + CSV_FILE_EXTENSION));

    if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
      return Optional.empty();
    }

    File outputFile = fileChooser.getSelectedFile();

    if (!outputFile.getName().toLowerCase().endsWith(CSV_FILE_EXTENSION)) {
      outputFile = new File(outputFile.getParentFile(), outputFile.getName() + CSV_FILE_EXTENSION);
    }

    if (!isFileNameAvailable(outputFile)) return Optional.empty();

    return Optional.of(outputFile);
  }

  private static File getDefaultDir() {
    var home = new File(System.getProperty("user.home"));
    var downloads = new File(home, DOWNLOADS_PATH);

    if (!downloads.exists()) {
      downloads = new File(home, TELECHARGEMENTS_PATH);
    }

    return downloads.exists() ? downloads : home;
  }

  private static String[] getFecHeader() {
    return new String[] {
      "JournalCode",
      "JournalLib",
      "EcritureNum",
      "EcritureDate",
      "CompteNum",
      "CompteLib",
      "CompAuxNum",
      "CompAuxLib",
      "PieceRef",
      "PieceDate",
      "EcritureLib",
      "Debit",
      "Credit",
      "EcritureLet",
      "DateLet",
      "ValidDate",
      "Montantdevise",
      "Idevise"
    };
  }

  private static String[] getFecLine(
      PossessionRecoupee possessionRecoupee,
      Set<PieceJustificative> pjs,
      int sequence,
      boolean isCreditor) {
    Possession possession = possessionRecoupee.possession();

    var currentjournalCode = JN;

    var journalCode = currentjournalCode.toString();
    var journalLib = currentjournalCode.getJournalLib();
    var ecritureNum = journalCode + String.format("%03d", sequence);
    var ecritureDate = formatFECDate(possession.t());

    var comptes = resolve(possession);
    var compteLib = getCompteLib(comptes, isCreditor);
    var compteNum = possession.getTypeFEC().abrev() + possession.getTypeFECCodeRegion();
    var compAuxNum = "";
    var compAuxLib = "";
    var pieceRef = getPieceRef(possession, pjs);
    var pieceDate = getPieceDate(possession, pjs);
    var ecritureLib = getEcritureLib(possession);

    getDebitOrCredit(possessionRecoupee, isCreditor);
    var debit = "";
    var credit = "";

    var ecritureLet = "";
    var dateLet = "";
    var validDate = "";
    var valeurRealiseEUR =
        possessionRecoupee.valeurRealisee().convertir(Devise.EUR, LocalDate.now());
    var valeurRealiseMGA = valeurRealiseEUR.convertir(Devise.MGA, LocalDate.now()).montant();
    var montantDevise = formatFECAmount(valeurRealiseMGA);
    var idevise = possessionRecoupee.valeurRealisee().devise().codeIso();

    return new String[] {
      journalCode, journalLib,
      ecritureNum, ecritureDate,
      compteNum, compteLib,
      compAuxNum, compAuxLib,
      pieceRef, pieceDate,
      ecritureLib, debit,
      credit, ecritureLet,
      dateLet, validDate,
      montantDevise, idevise
    };
  }

  private static boolean isFileNameAvailable(File file) {
    if (!file.exists()) {
      return true;
    }

    return JOptionPane.showConfirmDialog(
            null,
            "Le fichier existe déjà.\nVoulez-vous le remplacer ?",
            "Fichier existant",
            JOptionPane.YES_NO_OPTION)
        == JOptionPane.YES_OPTION;
  }
}
