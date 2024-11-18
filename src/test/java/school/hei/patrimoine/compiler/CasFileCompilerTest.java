package school.hei.patrimoine.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class CasFileCompilerTest {

  @SneakyThrows
  @Test
  void compile_file_to_cas() {

    ClassLoader classLoader = CasFileCompilerTest.class.getClassLoader();
    InputStream resourceStream = classLoader.getResourceAsStream("EtudiantPireCas.java");

    if (resourceStream == null) {
      throw new IllegalStateException(
          "Le fichier 'EtudiantPireCas.java' est introuvable dans les ressources.");
    }

    Path tempDir = Files.createTempDirectory("tempDirectory");
    Path tempFile = Paths.get(tempDir.toString(), "EtudiantPireCas.java");
    Files.createFile(tempFile);
    Files.copy(resourceStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

    CasFileCompiler casFileCompiler = new CasFileCompiler();
    var etudiantPireCasClass = casFileCompiler.apply(tempFile.toAbsolutePath().toString());
    var etudiantCas = etudiantPireCasClass.getDeclaredConstructor().newInstance();

    assertEquals("Ilo", etudiantCas.getClass().getDeclaredField("nom").get(etudiantCas));
    assertEquals(
        LocalDate.of(2000, 1, 1), etudiantCas.getClass().getDeclaredField("date").get(etudiantCas));
    assertEquals(3000, etudiantCas.getClass().getDeclaredField("devise").get(etudiantCas));

    Files.delete(tempFile);
  }
}
