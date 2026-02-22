package school.hei.patrimoine.patrilang.files;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static school.hei.patrimoine.patrilang.files.PatriLangFile.PatriLangFileType.CAS;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import lombok.Builder;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import school.hei.patrimoine.patrilang.files.validator.PatriLangFileValidator;

public class PatriLangFileWriter {
  private final PatriLangFileValidator validator;

  public PatriLangFileWriter() {
    this.validator = new PatriLangFileValidator();
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
        if (CAS.equals(input.file().getType())) {
          this.validator.accept(input.file());
          continue;
        }
        this.validator.accept(input.casSet());
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

  @Builder
  public record FileWriterInput(PatriLangFile file, PatriLangFile casSet, String content) {}

  @FunctionalInterface
  private interface ThrowingRunnable {
    void run() throws Exception;
  }
}
