package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.files.AppBar.ViewMode;

import java.awt.*;
import java.nio.file.Files;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.MarkdownToHtmlConverter;

public class HtmlViewer extends JEditorPane {
  private final MarkdownToHtmlConverter markdownToHtmlConverter;
  private final AppBar appBar;
  private final FileSideBar fileSideBar;

  public HtmlViewer(AppBar appBar, FileSideBar fileSideBar) {
    this.appBar = appBar;
    this.fileSideBar = fileSideBar;
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
    var currentMode = appBar.getCurrentMode();
    var currentFontSize = appBar.getControlledFontSize();
    var currentFile = fileSideBar.getSelectedFile().orElse(null);

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
