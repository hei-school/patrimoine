package school.hei.patrimoine.visualisation.swing.ihm.google.modele.fec.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import school.hei.patrimoine.modele.fec.EcritureComptable;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.fec.LigneEcriture;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.FileSelector;

public class FecExporter {
  private static final String CSV_EXTENSION = ".csv";

  public static void export(Journal journal, File selectedFile) throws FileNotFoundException {
    var outputFile =
        FileSelector.selectOutputFile("Exporter le fichier FEC", selectedFile, CSV_EXTENSION);

    if (outputFile.isEmpty()) return;

    try (var fecWriter = new FecWriter(outputFile.get())) {
      fecWriter.writeHeader();
      for (EcritureComptable ecriture : journal.ecritures()) {
        for (LigneEcriture ligne : ecriture.lignes()) {
          fecWriter.writeLine(journal, ecriture, ligne);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
