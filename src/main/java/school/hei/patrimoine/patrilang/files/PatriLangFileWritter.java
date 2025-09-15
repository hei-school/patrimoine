package school.hei.patrimoine.patrilang.files;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import lombok.Builder;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import school.hei.patrimoine.patrilang.validator.PatriLangValidator;

public class PatriLangFileWritter {
  private final PatriLangValidator validator;

  public PatriLangFileWritter() {
    this.validator = new PatriLangValidator();
  }

  public void write(FileWritterInput input) throws Exception {
    rollbackIfNotValid(() -> Files.writeString(input.file().toPath(), input.content()), input);
  }

  public void insertAtLine(FileWritterInput input, int lineIndex) throws Exception {
    rollbackIfNotValid(
        () -> {
          var tempFile = new File(input.file().getAbsolutePath() + ".tmp");

          var reader = new BufferedReader(new FileReader(input.file()));
          var writer = new BufferedWriter(new FileWriter(tempFile));

          String line;
          int current = 0;

          while ((line = reader.readLine()) != null) {
            if (current == lineIndex) {
              writer.write(input.content());
              writer.newLine();
            }
            writer.write(line);
            writer.newLine();
            current++;
          }

          if (lineIndex == current) {
            writer.write(input.content());
            writer.newLine();
          }

          Files.move(tempFile.toPath(), input.file().toPath(), StandardCopyOption.REPLACE_EXISTING);
        },
        input);
  }

  private void rollbackIfNotValid(ThrowingRunnable runnable, FileWritterInput input)
      throws Exception {
    var oldContent = Files.readString(input.file().toPath());

    try {
      runnable.run();
      validator.accept(input.casSet().getAbsolutePath());
    } catch (ParseCancellationException | IllegalArgumentException e) {
      Files.writeString(input.file().toPath(), oldContent);
      throw e;
    }
  }

  @Builder
  public record FileWritterInput(File file, File casSet, String content) {}

  @FunctionalInterface
  private interface ThrowingRunnable {
    void run() throws Exception;
  }
}
