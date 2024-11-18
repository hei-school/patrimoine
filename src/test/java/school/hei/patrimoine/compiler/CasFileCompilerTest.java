package school.hei.patrimoine.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.example.EtudiantPireCas;

class CasFileCompilerTest {

  @Test
  void convert_a_string_to_patrimoine()
      throws IOException,
          NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException {

    ClassLoader classLoader = CasFileCompilerTest.class.getClassLoader();
    InputStream resourceStream = classLoader.getResourceAsStream("files/EtudiantPireCas.java");

    if (resourceStream == null) {
      throw new IllegalStateException(
          "Le fichier 'EtudiantPireCas.java' est introuvable dans les ressources.");
    }

    Path tempDir = Files.createTempDirectory("tempDirectory");
    Path tempFile = Paths.get(tempDir.toString(), "EtudiantPireCas.java");
    Files.createFile(tempFile);
    Files.copy(resourceStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

    ClassNameExtractor classNameExtractor = new ClassNameExtractor();
    CasFileCompiler casFileCompiler = new CasFileCompiler();
    var etudiantPireCasClass = casFileCompiler.apply(tempFile.toAbsolutePath().toString());
    var etudiantCas = (EtudiantPireCas) etudiantPireCasClass.getDeclaredConstructor().newInstance();
    System.out.println(etudiantCas.patrimoine());

    assertEquals("EtudiantCas", classNameExtractor.apply(String.valueOf(etudiantCas)));

    Files.delete(tempFile);
  }
}
