package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import static javax.swing.event.HyperlinkEvent.EventType.ACTIVATED;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.PJ_FILE_EXTENSION;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar.ViewMode;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.patrilang.files.PatriLangFileWritter.FileWritterInput;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.MarkdownToHtmlConverter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

@Slf4j
public class HtmlViewer extends JEditorPane {
  private final State state;
  private final MarkdownToHtmlConverter markdownToHtmlConverter;
  private File lastEditedFile = null;
  private ViewMode lastViewMode = null;

  @Getter private final Map<File, String> originalContents = new HashMap<>();
  private final Map<File, FileWritterInput> modifiedFilesData = new HashMap<>();

  private static final Pattern URL_PATTERN = Pattern.compile("\"(https?://[^\"]+)\"");

  public HtmlViewer(State state) {
    this.state = state;
    this.markdownToHtmlConverter = new MarkdownToHtmlConverter();

    addEmptyContent();
    setBackground(new Color(255, 248, 220));

    addHyperlinkListener(
        e -> {
          if (e.getEventType() == ACTIVATED) {
            try {
              openUrlInBrowser(e.getURL().toString());
            } catch (Exception ex) {
              log.error("Erreur lors de l'ouverture du lien: {}", e.getURL(), ex);
            }
          }
        });

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
    if (ViewMode.EDIT.equals(lastViewMode)) {
      saveCurrentContentToMemory();
    }

    ViewMode currentMode = state.get("viewMode");
    File currentFile = state.get("selectedFile");
    int currentFontSize = state.get("fontSize");

    lastEditedFile = currentFile;
    lastViewMode = currentMode;

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

      content = convertQuotedUrlsToLinks(content);

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
    if (lastEditedFile == null || !lastEditedFile.exists() || !isEditable()) return;

    String originalContent = originalContents.get(lastEditedFile);
    if (originalContent == null) return;

    String currentContent = getText();
    if (currentContent.equals(originalContent)) {
      modifiedFilesData.remove(lastEditedFile);
      return;
    }

    if (lastEditedFile.getAbsolutePath().endsWith(PJ_FILE_EXTENSION)) {
      modifiedFilesData.put(
          lastEditedFile,
          FileWritterInput.builder().file(lastEditedFile).content(currentContent).build());
    } else {
      modifiedFilesData.put(
          lastEditedFile,
          FileWritterInput.builder()
              .file(lastEditedFile)
              .casSet(state.get("selectedCasSetFile"))
              .content(currentContent)
              .build());
    }
  }

  public Map<File, FileWritterInput> getModifiedFilesData() {
    return new HashMap<>(modifiedFilesData);
  }

  public void clearModifiedFiles() {
    modifiedFilesData.clear();
  }

  private String convertQuotedUrlsToLinks(String content) {
    Matcher matcher = URL_PATTERN.matcher(content);
    var result = new StringBuffer();

    while (matcher.find()) {
      String url = matcher.group(1);
      String replacement = "<a href=\"" + url + "\" target=\"_blank\">" + url + "</a>";
      matcher.appendReplacement(result, replacement);
    }
    matcher.appendTail(result);

    return result.toString();
  }

  private void openUrlInBrowser(String url) throws IOException {
    String os = System.getProperty("os.name").toLowerCase();
    ProcessBuilder processBuilder;

    if (os.contains("win")) {
      processBuilder = new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url);
    } else if (os.contains("mac")) {
      processBuilder = new ProcessBuilder("open", url);
    } else {
      processBuilder = new ProcessBuilder("xdg-open", url);
    }

    processBuilder.start();
  }
}
