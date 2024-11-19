package school.hei.patrimoine.google;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class GoogleApiTest {

  @Test
  void reset_directory_if_exist() throws Exception {
    Path tempDir = Files.createTempDirectory("test_reset");
    File testFile = new File(tempDir.toFile(), "test.txt");
    assertTrue(testFile.createNewFile());

    GoogleApi.resetIfExist(tempDir.toString());

    assertTrue(Files.exists(tempDir));
    assertEquals(0, Objects.requireNonNull(tempDir.toFile().listFiles()).length);

    Files.delete(tempDir);
  }

  @Test
  void copy_file_content() throws Exception {
    File sourceFile = File.createTempFile("source", ".txt");
    File targetFile = File.createTempFile("target", ".txt");
    Files.writeString(sourceFile.toPath(), "Test content");

    GoogleApi api = new GoogleApi();

    api.copyFileContent(sourceFile, targetFile);

    assertEquals("Test content", Files.readString(targetFile.toPath()));

    Files.delete(targetFile.toPath());
  }
}
