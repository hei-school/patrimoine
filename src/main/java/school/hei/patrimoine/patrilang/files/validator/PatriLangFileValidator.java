package school.hei.patrimoine.patrilang.files.validator;

import java.util.function.Consumer;
import school.hei.patrimoine.patrilang.files.PatriLangFile;

public class PatriLangFileValidator implements Consumer<PatriLangFile> {
  private final SupportingInfoFileValidator supportinInfoFileValidator;
  private final CasSetFileValidator casSetFileValidator;

  public PatriLangFileValidator() {
    this.supportinInfoFileValidator = new SupportingInfoFileValidator();
    this.casSetFileValidator = new CasSetFileValidator();
  }

  @Override
  public void accept(PatriLangFile file) {
    switch (file.getType()) {
      case SUPPORTING_INFO -> this.supportinInfoFileValidator.accept(file);
      case TOUT_CAS -> this.casSetFileValidator.accept(file);
      default -> throw new IllegalArgumentException("Type invalide pour un fichier à valider");
    }
  }
}
