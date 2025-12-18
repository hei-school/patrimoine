package school.hei.patrimoine.patrilang.files;

public class PatriLangFileContext {
  private final PatriLangFileWritter.FileWritterInput input;

  public PatriLangFileContext(PatriLangFileWritter.FileWritterInput input) {
    this.input = input;
  }

  public boolean isPlanned() {
    return input.file().getAbsolutePath().replace("\\", "/").contains("/download/planifies");
  }
}
