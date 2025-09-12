package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.AppBar.ViewMode;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Set;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.MarkdownToHtmlConverter;

public class HtmlViewer extends JEditorPane {
  private final State state;
  private final MarkdownToHtmlConverter markdownToHtmlConverter;

  public HtmlViewer(State state) {
    this.state = state;
    this.markdownToHtmlConverter = new MarkdownToHtmlConverter();

    addEmptyContent();
    setBackground(new Color(255, 248, 220));

    state.subscribe(Set.of("viewMode", "fontSize", "selectedFile"), this::update);
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
    ViewMode currentMode = state.get("viewMode");
    File currentFile = state.get("selectedFile");
    int currentFontSize = state.get("fontSize");

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
