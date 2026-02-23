package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static school.hei.patrimoine.patrilang.files.PatriLangFile.PatriLangFileType.CAS;
import static school.hei.patrimoine.patrilang.files.PatriLangFile.PatriLangFileType.TOUT_CAS;
import static school.hei.patrimoine.visualisation.swing.ihm.google.config.EnvironmentConfig.isOnlineMode;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkListDownloader.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext.PatriLangFileContextType;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext.PatriLangFileContextType.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import school.hei.patrimoine.patrilang.files.PatriLangFile;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

public class FilesProvider {
  public static List<PatriLangFileContext> getPatriLangJustificativeFiles() {
    return Arrays.stream(requireNonNull(new File(getJustificativeDirectoryPath()).listFiles()))
        .map(PatriLangFile::new)
        .map(file -> toContext(file, PJ))
        .toList();
  }

  public static List<PatriLangFileContext> getPatriLangPlannedFiles() {
    return Arrays.stream(requireNonNull(new File(getPlannedDirectoryPath()).listFiles()))
        .map(PatriLangFile::new)
        .map(file -> toContext(file, PLANNED))
        .toList();
  }

  public static List<PatriLangFileContext> getPatriLangDoneFiles() {
    return Arrays.stream(requireNonNull(new File(getDoneDirectoryPath()).listFiles()))
        .map(PatriLangFile::new)
        .map(file -> toContext(file, DONE))
        .toList();
  }

  public static PatriLangFileContext getPlannedCasSetFile() {
    return getPatriLangPlannedFiles().stream()
         .filter(PatriLangFile::isTypeToutCas)
        .findFirst()
        .orElseThrow();
  }

  public static PatriLangFileContext getDoneCasSetFile() {
    return getPatriLangDoneFiles().stream()
        .filter(PatriLangFile::isTypeToutCas)
        .findFirst()
        .orElseThrow();
  }

  public static List<PatriLangFileContext> getDonePatrilangFilesWithoutCasSet() {
    return getPatriLangDoneFiles().stream()
        .filter(file -> !TOUT_CAS.equals(file.getType()))
        .toList();
  }

  public static PatriLangFileContext getCasSet(PatriLangFileContext file) {
    return switch (file.getContext()) {
      case DONE, PJ -> getDoneCasSetFile();
      case PLANNED -> getPlannedCasSetFile();
    };
  }

  public static Optional<PatriLangFileContext> getPJ(PatriLangFileContext file) {
    if (!CAS.equals(file.getType()) || DONE.equals(file.getContext())) {
      return Optional.empty();
    }

    var casName = file.getBaseFileName();
    return getPatriLangJustificativeFiles().stream()
        .filter(item -> item.getBaseFileName().equals(casName))
        .findFirst();
  }

  public static PatriLangFileContext getPlannedFile(PatriLangFileContext toFind) {
    return getPatriLangPlannedFiles().stream()
        .filter(file -> toFind.getBaseFileName().equals(file.getBaseFileName()))
        .findFirst()
        .orElseThrow();
  }

  public static PatriLangFileContext getDoneFile(PatriLangFileContext toFind) {
    return getPatriLangDoneFiles().stream()
        .filter(file -> toFind.getBaseFileName().equals(file.getBaseFileName()))
        .findFirst()
        .orElseThrow();
  }

  private static List<GoogleLinkList.NamedID> getIds(PatriLangFileContextType context) {
    GoogleLinkList<GoogleLinkList.NamedID> ids = AppContext.getDefault().getData("named-ids");
    if(ids == null){
      return List.of();
    }
    return switch (context) {
      case DONE -> ids.done();
      case PLANNED -> ids.planned();
      case PJ -> ids.justificative();
    };
  }

  private static PatriLangFileContext toContext(
      PatriLangFile file, PatriLangFileContextType context) {
    return new PatriLangFileContext(file, getDriveId(file, context), context);
  }

  private static String getDriveId(PatriLangFile file, PatriLangFileContextType context) {
    var ids = getIds(context);
    var id = ids.stream()
        .filter(nameId -> nameId.name().equals(file.getBaseFileName()))
        .map(GoogleLinkList.NamedID::id)
        .findFirst();

    if(id.isPresent()) {
      return id.get();
    }

    if(isOnlineMode() ){
      throw new RuntimeException("Online mode without id");
    }

    return randomUUID().toString();
  }
}
