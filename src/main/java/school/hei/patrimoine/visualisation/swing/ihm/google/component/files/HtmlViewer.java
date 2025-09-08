package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.files.AppBar.ViewMode;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.function.Supplier;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.MarkdownToHtmlConverter;

public class HtmlViewer extends JEditorPane {
  private final Supplier<File> currentFileSupplier;
  private final Supplier<ViewMode> currentModeSupplier;
  private final Supplier<Integer> currentFontSizeSupplier;
  private final MarkdownToHtmlConverter markdownToHtmlConverter;

  public HtmlViewer(
      Supplier<ViewMode> currentModeSupplier,
      Supplier<Integer> currentFontSizeSupplier,
      Supplier<File> currentFileSupplier) {
    this.currentModeSupplier = currentModeSupplier;
    this.currentFontSizeSupplier = currentFontSizeSupplier;
    this.currentFileSupplier = currentFileSupplier;
    this.markdownToHtmlConverter = new MarkdownToHtmlConverter();

    addEmptyContent();
    setBackground(new Color(255, 248, 220));
  }

  private void addEmptyContent() {
    setContentType("text/html");
    setEditable(false);
    setText(
        "<html><body style='background-color:#fff8dc;'>Sélectionner un fichier pour"
            + " l'afficher.</body></html>");
  }

  public JScrollPane toScrollPane() {
    return new JScrollPane(this);
  }

  public void update() {
    var currentMode = currentModeSupplier.get();
    var currentFontSize = currentFontSizeSupplier.get();
    var currentFile = currentFileSupplier.get();

    if (currentFile == null || !currentFile.exists()) {
      addEmptyContent();
      return;
    }

    try {
      var content = Files.readString(currentFile.toPath());
      setFont(new Font(Font.MONOSPACED, Font.PLAIN, currentFontSize));

      if (ViewMode.EDIT.equals(currentMode)) {
        setContentType("text/plain");
        setEditable(true);
        setText(content);
        setCaretPosition(0);
        return;
      }

      var html = markdownToHtmlConverter.apply(content);
      html = html.replace("<body>", "<body style='font-size: " + currentFontSize + "px;'>");
      html = html.replaceAll("<code>", "<code style='font-size: " + currentFontSize + "px;'>");

      setContentType("text/html");
      setEditable(false);
      setText(html);
      setCaretPosition(0);
    } catch (Exception ex) {
      setText("<html><body style='color:red;'>Erreur lors de la lecture du fichier.</body></html>");
    }
  }
}
