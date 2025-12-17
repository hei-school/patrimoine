package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar.ViewMode;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import school.hei.patrimoine.patrilang.files.PatriLangFileWritter.FileWritterInput;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.MarkdownToHtmlConverter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class HtmlViewer extends JEditorPane {
  private final State state;
  private final MarkdownToHtmlConverter markdownToHtmlConverter;
  private File lastEditedFile = null;

  private final Map<File, String> originalContents = new HashMap<>();
  private final Map<File, FileWritterInput> modifiedFilesData = new HashMap<>();

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
    saveCurrentContentToMemory();

    ViewMode currentMode = state.get("viewMode");
    File currentFile = state.get("selectedFile");
    int currentFontSize = state.get("fontSize");

    lastEditedFile = currentFile;

    if (currentFile == null || !currentFile.exists()) {
      addEmptyContent();
      return;
    }

    try {
      String content;
      if (modifiedFilesData.containsKey(currentFile)) {
        content = modifiedFilesData.get(currentFile).content();
      } else {
        content = Files.readString(currentFile.toPath());
        if (ViewMode.EDIT.equals(currentMode)) {
          originalContents.putIfAbsent(currentFile, content);
        }
      }

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

  public void saveCurrentContentToMemory() {
    if (lastEditedFile == null || !lastEditedFile.exists()) return;

    ViewMode currentMode = state.get("viewMode");
    if (!ViewMode.EDIT.equals(currentMode) || !isEditable()) return;

    String originalContent = originalContents.get(lastEditedFile);
    if (originalContent == null) return;

    String currentContent = getText();
    if (currentContent.equals(originalContent)) {
      modifiedFilesData.remove(lastEditedFile);
      return;
    }

    File currentCasSet = state.get("selectedCasSetFile");

    modifiedFilesData.put(
        lastEditedFile,
        FileWritterInput.builder()
            .file(lastEditedFile)
            .casSet(currentCasSet)
            .content(currentContent)
            .build());
  }

  public Map<File, FileWritterInput> getModifiedFilesData() {
    return new HashMap<>(modifiedFilesData);
  }

  public void clearModifiedFiles() {
    modifiedFilesData.clear();
    originalContents.clear();
  }
}
