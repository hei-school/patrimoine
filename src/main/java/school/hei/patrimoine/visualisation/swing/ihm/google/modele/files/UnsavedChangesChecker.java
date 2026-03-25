package school.hei.patrimoine.visualisation.swing.ihm.google.modele.files;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode.EDIT;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.getAllModifiedFiles;

import java.util.Set;
import java.util.stream.Collectors;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.HtmlViewer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode;

public class UnsavedChangesChecker {
  private static void flushEditorIfNeeded(ViewMode currentMode, HtmlViewer htmlViewer) {
    if (EDIT.equals(currentMode)) {
      htmlViewer.saveLastFileToTempContent();
    }
  }

  public static boolean hasUnsavedChanges(ViewMode currentMode, HtmlViewer htmlViewer) {
    flushEditorIfNeeded(currentMode, htmlViewer);
    return !getAllModifiedFiles().isEmpty();
  }

  public static Set<String> getUnsavedFileNames(ViewMode currentMode, HtmlViewer htmlViewer) {
    flushEditorIfNeeded(currentMode, htmlViewer);
    return getAllModifiedFiles().stream()
        .map(input -> input.file().getBaseFileName())
        .collect(Collectors.toSet());
  }
}
