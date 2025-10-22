package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.*;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;

@Builder
@Slf4j
public class AsyncTask<T> {
  private final Task<T> task;

  @Builder.Default private final String loadingMessage = "Chargement en cours...";
  @Builder.Default private final Dimension dialogDimension = new Dimension(300, 100);
  @Builder.Default private final Consumer<T> onSuccess = (data) -> {};
  @Builder.Default private final Consumer<Exception> onError = (e) -> {};
  @Builder.Default private final boolean withDialogLoading = true;

  public interface Task<T> {
    T run() throws Exception;
  }

  public void execute() {
    Dialog dialog;

    if (withDialogLoading) {
      dialog = new Dialog(loadingMessage, dialogDimension.width, dialogDimension.height);
    } else {
      dialog = null;
    }

    SwingWorker<T, T> worker =
        new SwingWorker<>() {
          @Override
          protected T doInBackground() throws Exception {
            return task.run();
          }

          @Override
          protected void done() {
            try {
              onSuccess.accept(get());
            } catch (Exception e) {
              Throwable cause =
                  e instanceof ExecutionException ? e.getCause() : new RuntimeException(e);
              log.error(cause.getMessage());
              Arrays.stream(cause.getStackTrace())
                  .forEach(
                      error -> {
                        log.error(error.toString());
                      });

              if (cause instanceof Exception exception) {
                onError.accept(exception);
              } else {
                onError.accept(new RuntimeException(cause));
              }
            }

            if (withDialogLoading) {
              dialog.setVisible(false);
              dialog.dispose();
            }
          }
        };

    worker.execute();
    if (withDialogLoading) {
      dialog.setVisible(true);
    }
  }
}
