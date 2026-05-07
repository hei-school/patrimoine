package school.hei.patrimoine.modele.comptable.fec.io;

import static com.opencsv.ICSVWriter.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newOutputStream;
import static school.hei.patrimoine.modele.comptable.fec.FECLine.headers;

import com.opencsv.CSVWriter;
import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import school.hei.patrimoine.modele.comptable.fec.Journal;
import school.hei.patrimoine.modele.comptable.fec.mapper.CompteNumResolver;
import school.hei.patrimoine.modele.comptable.fec.mapper.FECLineMapper;

public class FECWriter implements Closeable {
  private final CSVWriter csvWriter;

  public FECWriter(Path path) throws IOException {
    var writer = new BufferedWriter(new OutputStreamWriter(newOutputStream(path), UTF_8));
    this.csvWriter =
        new CSVWriter(writer, '\t', NO_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END);
  }

  public void writeFEC(Collection<Journal> journals) {
    csvWriter.writeNext(headers());

    var mapper = new FECLineMapper(new CompteNumResolver());
    for (var journal : journals) {
      for (var ecriture : journal.ecritures()) {
        for (var ligne : ecriture.lignes()) {
          var line = mapper.toFECLine(journal, ecriture, ligne);
          csvWriter.writeNext(line.values());
        }
      }
    }
  }

  @Override
  public void close() throws IOException {
    csvWriter.close();
  }
}
