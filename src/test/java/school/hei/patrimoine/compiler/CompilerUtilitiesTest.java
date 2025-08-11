package school.hei.patrimoine.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.patrimoine.compiler.CompilerUtilities.copyFileContent;
import static school.hei.patrimoine.compiler.CompilerUtilities.resetIfExist;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class CompilerUtilitiesTest {
  @Test
  void copy_file_content() throws Exception {
    var sourceFile = File.createTempFile("source", ".txt");
    var targetFile = File.createTempFile("target", ".txt");

    Files.writeString(sourceFile.toPath(), "Test content");

    copyFileContent(sourceFile, targetFile);

    assertEquals("Test content", Files.readString(targetFile.toPath()));

    Files.delete(targetFile.toPath());
  }

  @Test
  void reset_directory_if_exist() throws Exception {
    Path tempDir = Files.createTempDirectory("test_reset");
    File testFile = new File(tempDir.toFile(), "test.txt");
    assertTrue(testFile.createNewFile());

    resetIfExist(tempDir.toString());

    assertTrue(Files.exists(tempDir));
    assertEquals(0, Objects.requireNonNull(tempDir.toFile().listFiles()).length);

    Files.delete(tempDir);
  }
}
