package school.hei.patrimoine.modele.fec;

import static com.opencsv.ICSVWriter.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newOutputStream;
import static school.hei.patrimoine.modele.fec.FECLine.header;
import static school.hei.patrimoine.modele.fec.mapper.FECLineMapper.toFECLine;

import com.opencsv.CSVWriter;
import java.io.*;
import java.nio.file.Path;
import java.util.Collection;

public class FECWriter implements Closeable {
  private final Writer writer;
  private final CSVWriter csvWriter;

  private static final char UTF8_BOM = '\uFEFF';

  public FECWriter(Path path) throws IOException {
    this.writer = new OutputStreamWriter(newOutputStream(path), UTF_8);
    writer.write(UTF8_BOM);

    this.csvWriter =
        new CSVWriter(writer, '|', NO_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END);
  }

  public void writeFEC(Collection<Journal> journals) {
    writeHeader();
    writeLines(journals);
  }

  private void writeLines(Collection<Journal> journals) {
    for (var journal : journals) {
      for (var ecriture : journal.ecritures()) {
        for (var ligne : ecriture.lignes()) {
          writeLine(journal, ecriture, ligne);
        }
      }
    }
  }

  private void writeHeader() {
    csvWriter.writeNext(header());
  }

  private void writeLine(Journal journal, EcritureComptable ecriture, LigneEcriture ligne) {
    csvWriter.writeNext(toFECLine(journal, ecriture, ligne).toArray());
  }

  @Override
  public void close() throws IOException {
    csvWriter.close();
    writer.close();
  }
}
