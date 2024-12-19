package school.hei.patrimoine.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.function.Function;
import lombok.SneakyThrows;

public class PackageNameExtractor implements Function<String, String> {

  @SneakyThrows
  @Override
  public String apply(String filePath) {
    File file = new File(filePath);
    final String packagePrefix = "package ";

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.startsWith(packagePrefix)) {
          return line.substring(packagePrefix.length(), line.indexOf(";")).trim();
        }
      }
    }
    throw new IllegalArgumentException("No package name found in the provided file: " + filePath);
  }
}
