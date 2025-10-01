package school.hei.patrimoine.compiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompilerUtilities {
  private CompilerUtilities() {}

  public static final String USER_HOME = System.getProperty("user.home");
  public static final String COMPILE_DIR_NAME = USER_HOME + "/.patrimoine/compile";
  public static final String DOWNLOADS_DIRECTORY_PATH = USER_HOME + "/.patrimoine/drive";
  private static final String PATRIMOINE_JAR_NAME = "patrimoine-1.0-SNAPSHOT.jar";

  public static final String PATRIMOINE_JAR_PATH =
      String.valueOf(Path.of(DOWNLOADS_DIRECTORY_PATH).resolve(PATRIMOINE_JAR_NAME));

  static {
    resetIfExist(COMPILE_DIR_NAME);
  }

  @SuppressWarnings("all")
  public static void resetIfExist(String directoryPath) {
    Path path = Paths.get(directoryPath);
    File directory = path.toFile();

    try {
      if (directory.exists()) {
        Files.walk(path).map(Path::toFile).forEach(File::delete);
      }
      Files.createDirectories(path);
    } catch (IOException e) {
      throw new RuntimeException("Directory reset showError : " + directoryPath, e);
    }
  }
}
