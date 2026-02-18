package school.hei.patrimoine.patrilang.files;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.PJ_FILE_EXTENSION;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpilePieceJustificative;

import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Builder;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import school.hei.patrimoine.patrilang.validator.PatriLangValidator;

public class PatriLangFileWriter {
  private final PatriLangValidator validator;

  public PatriLangFileWriter() {
    this.validator = new PatriLangValidator();
  }

  public void write(Collection<FileWriterInput> inputs) throws Exception {
    rollbackIfNotValid(
        () -> {
          for (var input : inputs) {
            writeString(input.file().toPath(), input.content(), UTF_8);
          }
        },
        inputs);
  }

  public void insertAtLine(FileWriterInput input, int lineIndex) throws Exception {
    rollbackIfNotValid(
        () -> {
          var tempFile = new File(input.file().getAbsolutePath() + ".tmp");
          var reader = newBufferedReader(input.file().toPath(), UTF_8);
          var writer = newBufferedWriter(tempFile.toPath(), UTF_8, CREATE, TRUNCATE_EXISTING);

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
          move(tempFile.toPath(), input.file().toPath(), REPLACE_EXISTING);
        },
        Set.of(input));
  }

  private void rollbackIfNotValid(ThrowingRunnable runnable, Collection<FileWriterInput> inputs)
      throws Exception {
    var oldContents = getContents(inputs);

    try {
      runnable.run();
      for (var input : inputs) {
        validate(input);
      }
    } catch (ParseCancellationException | IllegalArgumentException error) {
      oldContents.forEach(
          (path, value) -> {
            try {
              writeString(Path.of(path), value, UTF_8);
            } catch (IOException exception) {
              throw new RuntimeException(exception);
            }
          });
      throw error;
    }
  }

  private static Map<String, String> getContents(Collection<FileWriterInput> inputs)
      throws IOException {
    Map<String, String> contents = new HashMap<>();
    for (var input : inputs) {
      var oldContent = readString(input.file().toPath());
      contents.put(input.file().getAbsolutePath(), oldContent);
    }
    return contents;
  }

  private void validate(FileWriterInput input) {
    var filePath = input.file().getAbsolutePath();
    if (filePath.endsWith(PJ_FILE_EXTENSION)) {
      transpilePieceJustificative(filePath);
      return;
    }
    validator.accept(input.casSet().getAbsolutePath());
  }

  @Builder
  public record FileWriterInput(File file, File casSet, String content) {}

  @FunctionalInterface
  private interface ThrowingRunnable {
    void run() throws Exception;
  }
}
