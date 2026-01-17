package school.hei.patrimoine.patrilang.files;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.PJ_FILE_EXTENSION;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpilePieceJustificative;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import lombok.Builder;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import school.hei.patrimoine.patrilang.validator.PatriLangValidator;

public class PatriLangFileWritter {
  private final PatriLangValidator validator;

  public PatriLangFileWritter() {
    this.validator = new PatriLangValidator();
  }

  public void write(FileWritterInput input) throws Exception {
    rollbackIfNotValid(
        () -> Files.writeString(input.file().toPath(), input.content(), StandardCharsets.UTF_8),
        input);
  }

  public void insertAtLine(FileWritterInput input, int lineIndex) throws Exception {
    rollbackIfNotValid(
        () -> {
          var tempFile = new File(input.file().getAbsolutePath() + ".tmp");
          var reader = Files.newBufferedReader(input.file().toPath(), StandardCharsets.UTF_8);
          var writer =
              Files.newBufferedWriter(
                  tempFile.toPath(),
                  StandardCharsets.UTF_8,
                  StandardOpenOption.CREATE,
                  StandardOpenOption.TRUNCATE_EXISTING);

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
          reader.close();
          writer.close();
          Files.move(tempFile.toPath(), input.file().toPath(), StandardCopyOption.REPLACE_EXISTING);
        },
        input);
  }

  private void rollbackIfNotValid(ThrowingRunnable runnable, FileWritterInput input)
      throws Exception {
    var oldContent = Files.readString(input.file().toPath());
    try {
      runnable.run();

      var filePath = input.file().getAbsolutePath();
      if (filePath.endsWith(PJ_FILE_EXTENSION)) {
        transpilePieceJustificative(filePath);
        return;
      }

      validator.accept(input.casSet().getAbsolutePath());
    } catch (ParseCancellationException | IllegalArgumentException e) {
      Files.writeString(input.file().toPath(), oldContent, StandardCharsets.UTF_8);
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
