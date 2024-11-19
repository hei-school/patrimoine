package school.hei.patrimoine.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.example.PatrimoineRicheSupplier;

class PatrimoineFileCompilerTest {

  @SneakyThrows
  @Test
  void convert_file_to_patrimoine() {

    ClassLoader classLoader = PatrimoineFileCompilerTest.class.getClassLoader();
    InputStream resourceStream = classLoader.getResourceAsStream("PatrimoineRicheSupplier.java");

    if (resourceStream == null) {
      throw new IllegalStateException(
          "Le fichier 'PatrimoineRicheSupplier.java' est introuvable dans les ressources.");
    }

    Path tempDir = Files.createTempDirectory("tempDirectory");
    Path tempFile = Paths.get(tempDir.toString(), "PatrimoineRicheSupplier.java");
    Files.createFile(tempFile);
    Files.copy(resourceStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

    PatrimoineRicheSupplier patrimoineRicheSupplier = new PatrimoineRicheSupplier();

    PatrimoineFileCompiler patrimoineFileCompiler = new PatrimoineFileCompiler();
    var patrimoineRiche = patrimoineFileCompiler.apply(tempFile.toAbsolutePath().toString());

    assertEquals(patrimoineRicheSupplier.get().nom(), patrimoineRiche.nom());

    Files.delete(tempFile);
  }
}
