package school.hei.patrimoine.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class PackageNameExtractorTest {

  @SneakyThrows
  @Test
  void extract_class_name_from_string_ok() {
    ClassLoader classLoader = PackageNameExtractorTest.class.getClassLoader();
    URL resourceUrl = classLoader.getResource("PatrimoineRicheSupplier.java");
    Path resourcePath = Paths.get(resourceUrl.toURI());

    PackageNameExtractor packageNameExtractor = new PackageNameExtractor();
    var packageName = packageNameExtractor.apply(String.valueOf(resourcePath));

    assertEquals(packageName, "patrimoine.drive");
  }
}
