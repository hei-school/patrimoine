package school.hei.patrimoine.visualisation.swing.ihm.google.modele.files;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import school.hei.patrimoine.patrilang.files.PatriLangFile;

@Getter
public class PatriLangFileContext extends PatriLangFile {
  private final String driveId;
  private final PatriLangFileContextType context;

  public PatriLangFileContext(
      @NonNull PatriLangFile file, String driveId, PatriLangFileContextType context) {
    super(file);
    this.context = context;
    this.driveId = driveId;
  }

  public boolean isPlanned() {
    return PatriLangFileContextType.PLANNED.equals(context);
  }

  public boolean isDone() {
    return PatriLangFileContextType.DONE.equals(context);
  }

  public boolean isPJ() {
    return PatriLangFileContextType.PJ.equals(context);
  }

  public enum PatriLangFileContextType {
    PJ,
    DONE,
    PLANNED
  }
}
