package school.hei.patrimoine.visualisation.swing.ihm.google.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import school.hei.patrimoine.patrilang.validator.PatriLangValidator;

public class PatriLangFileWritter {
  private final PatriLangValidator validator;

  public PatriLangFileWritter() {
    this.validator = new PatriLangValidator();
  }

  public void write(Supplier<String> content, File file, File casSet) throws Exception {
    rollbackIfNotValid(
        file,
        casSet,
        () -> {
          Files.writeString(file.toPath(), content.get());
        });
  }

  public void insertAtLine(Supplier<String> content, File file, int lineIndex, File casSet)
      throws Exception {
    rollbackIfNotValid(
        file,
        casSet,
        () -> {
          File tempFile = new File(file.getAbsolutePath() + ".tmp");

          try (BufferedReader reader = new BufferedReader(new FileReader(file));
              BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            int current = 0;

            while ((line = reader.readLine()) != null) {
              if (current == lineIndex) {
                writer.write(content.get());
                writer.newLine();
              }
              writer.write(line);
              writer.newLine();
              current++;
            }

            if (lineIndex == current) {
              writer.write(content.get());
              writer.newLine();
            }
          }

          Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        });
  }

  private void rollbackIfNotValid(File file, File casSet, ThrowingRunnable action)
      throws Exception {
    var oldContent = Files.readString(file.toPath());

    try {
      action.run();
      validator.accept(casSet.getAbsolutePath());
    } catch (ParseCancellationException | IllegalArgumentException e) {
      if (file.exists()) {
        Files.writeString(file.toPath(), oldContent); // rollback
      }
      throw e;
    }
  }

  @FunctionalInterface
  private interface ThrowingRunnable {
    void run() throws Exception;
  }
}
