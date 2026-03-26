package school.hei.patrimoine.modele.comptable.fec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import school.hei.patrimoine.modele.comptable.fec.io.FECWriter;

public record FEC(List<Journal> journals) {
  public File export(Path outputPath) throws IOException {
    try (var fecWriter = new FECWriter(outputPath)) {
      fecWriter.writeFEC(journals);
    }
    return outputPath.toFile();
  }
}
