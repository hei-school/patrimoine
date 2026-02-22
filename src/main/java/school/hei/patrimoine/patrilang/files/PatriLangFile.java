package school.hei.patrimoine.patrilang.files;

import java.io.File;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

@Getter
public class PatriLangFile extends File {
  private final PatriLangFileType type;

  public PatriLangFile(@NonNull String pathname) {
    super(pathname);
    this.type = PatriLangFileType.from(this);
  }

  public PatriLangFile(File file) {
    this(file.getAbsolutePath());
  }

  public PatriLangFile(@NonNull String pathname, PatriLangFileType type) {
    super(pathname);
    this.type = type;
  }

  public String getBaseFileName() {
    return getName()
        .replaceAll(PJ_FILE_EXTENSION, "")
        .replaceAll(TOUT_CAS_FILE_EXTENSION, "")
        .replaceAll(CAS_FILE_EXTENSION, "");
  }

  public boolean isTypePJ() {
    return PatriLangFileType.PJ.equals(getType());
  }

  public boolean isTypeCas() {
    return PatriLangFileType.CAS.equals(getType());
  }

  public boolean isTypeToutCas() {
    return PatriLangFileType.TOUT_CAS.equals(getType());
  }

  public static final String PJ_FILE_EXTENSION = ".pj.md";
  public static final String CAS_FILE_EXTENSION = ".cas.md";
  public static final String TOUT_CAS_FILE_EXTENSION = ".tout.md";

  public enum PatriLangFileType {
    PJ,
    CAS,
    TOUT_CAS;

    public static PatriLangFileType from(File file) {
      var filename = file.getName();

      if (filename.endsWith(PJ_FILE_EXTENSION)) {
        return PJ;
      }

      if (filename.endsWith(CAS_FILE_EXTENSION)) {
        return CAS;
      }

      if (filename.endsWith(TOUT_CAS_FILE_EXTENSION)) {
        return TOUT_CAS;
      }

      throw new IllegalArgumentException(
          "Type de fichier inconnu pour '"
              + filename
              + "'. Extensions attendues : "
              + PJ_FILE_EXTENSION
              + ", "
              + CAS_FILE_EXTENSION
              + ", "
              + TOUT_CAS_FILE_EXTENSION);
    }
  }
}
