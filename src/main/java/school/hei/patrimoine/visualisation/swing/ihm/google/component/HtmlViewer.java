package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import static school.hei.patrimoine.visualisation.swing.ihm.google.PatriLangViewerScreen.ViewMode;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.MarkdownToHtmlConverter;

public class HtmlViewer extends JEditorPane {
  private final MarkdownToHtmlConverter markdownToHtmlConverter;

  public HtmlViewer() {
    this.markdownToHtmlConverter = new MarkdownToHtmlConverter();

    setContentType("text/html");
    setEditable(false);
    setText(
        "<html><body style='background-color:#fff8dc;'>Sélectionner un fichier pour"
            + " l'afficher.</body></html>");
  }

  public JScrollPane toScrollPane() {
    return new JScrollPane(this);
  }

  public void update(ViewMode currentMode, File currentFile, int fontSize) {
    if (currentFile == null || !currentFile.exists()) {
      setText(
          "<html><body style='background-color:#fff8dc;'>Sélectionner un fichier pour"
              + " l'afficher.</body></html>");
      return;
    }

    try {
      var content = Files.readString(currentFile.toPath());
      setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));

      if (currentMode == ViewMode.EDIT) {
        setContentType("text/plain");
        setEditable(true);
        setBackground(new Color(255, 248, 220));
        setText(content);
      } else {
        var html = markdownToHtmlConverter.apply(content);
        html = html.replace("<body>", "<body style='font-size: " + fontSize + "px;'>");
        html = html.replaceAll("<code>", "<code style='font-size: " + fontSize + "px;'>");
        setContentType("text/html");
        setEditable(false);
        setBackground(Color.WHITE);
        setText(html);
      }
      setCaretPosition(0);
    } catch (Exception ex) {
      setText("<html><body style='color:red;'>Erreur lors de la lecture du fichier.</body></html>");
    }
  }
}
