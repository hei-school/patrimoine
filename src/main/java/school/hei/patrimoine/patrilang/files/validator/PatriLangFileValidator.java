package school.hei.patrimoine.patrilang.files.validator;

import java.util.function.Consumer;
import school.hei.patrimoine.patrilang.files.PatriLangFile;

public class PatriLangFileValidator implements Consumer<PatriLangFile> {
  private final PJFileValidator pjFileValidator;
  private final CasSetFileValidator casSetFileValidator;

  public PatriLangFileValidator() {
    this.pjFileValidator = new PJFileValidator();
    this.casSetFileValidator = new CasSetFileValidator();
  }

  @Override
  public void accept(PatriLangFile file) {
    switch (file.getType()) {
      case PJ -> this.pjFileValidator.accept(file);
      case TOUT_CAS -> this.casSetFileValidator.accept(file);
      default -> throw new IllegalArgumentException("Type invalide pour un fichier à valider");
    }
  }
}
