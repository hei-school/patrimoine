package school.hei.patrimoine.visualisation.swing.ihm.google.modele.files;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PatriLangStagingFileManager {
  private static final Map<String, PatriLangFileContext> staged = new ConcurrentHashMap<>();

  public static List<PatriLangFileContext> getFiles() {
    return new ArrayList<>(staged.values());
  }

  public static List<PatriLangFileContext> getPlannedFiles() {
    return getFiles().stream().filter(PatriLangFileContext::isPlanned).toList();
  }

  public static List<PatriLangFileContext> getDoneFiles() {
    return getFiles().stream().filter(PatriLangFileContext::isDone).toList();
  }

  public static List<PatriLangFileContext> getPJFiles() {
    return getFiles().stream().filter(PatriLangFileContext::isPJ).toList();
  }

  public static void stage(PatriLangFileContext file) {
    staged.put(file.getDriveId(), file);
  }

  public static void unstage(PatriLangFileContext file) {
    staged.remove(file.getDriveId());
  }

  public static void unstageAll() {
    staged.clear();
  }
}
