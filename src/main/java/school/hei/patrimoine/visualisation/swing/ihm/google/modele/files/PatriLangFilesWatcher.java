package school.hei.patrimoine.visualisation.swing.ihm.google.modele.files;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getDoneCasSetFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getPlannedCasSetFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.recouppement.model.CompteGetter;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class PatriLangFilesWatcher {
  private final List<State.StateObserverCallbackRunnable> observers;

  private PatriLangFilesWatcher() {
    this.observers = new ArrayList<>();
    globalState().subscribe(Set.of(ANY_FILE_MODIFIED), this::update);
  }

  public void update() {
    if (!isAnyFileModified()) {
      return;
    }
    var done = transpileToutCas(getDoneCasSetFile());
    var planned = transpileToutCas(getPlannedCasSetFile());

    globalState()
        .update(
            Map.of(
                DONE_CAS_SET, done,
                PLANNED_CAS_SET, planned,
                DONE_CAS_SET_COMPTES, CompteGetter.make(done)));
    this.observers.forEach(State.StateObserverCallbackRunnable::run);
    globalState().update(ANY_FILE_MODIFIED, false);
  }

  public static String DONE_CAS_SET = "DONE_CAS_SET";
  public static String PLANNED_CAS_SET = "PLANNED_CAS_SET";
  public static String ANY_FILE_MODIFIED = "ANY_FILE_MODIFIED";
  public static String DONE_CAS_SET_COMPTES = "DONE_CAS_SET_COMPTES";

  public static CasSet getCasSet(PatriLangFileContext file) {
    return switch (file.getContext()) {
      case DONE, PJ -> getDoneCasSet();
      case PLANNED -> getPlannedCasSet();
    };
  }

  public static Cas getCas(PatriLangFileContext file) {
    var baseName = file.getBaseFileName();
    // TODO: throw if PJ
    return getCasSet(file).set().stream()
        .filter(cas -> cas.patrimoine().nom().equals(baseName))
        .findFirst()
        .orElseThrow();
  }

  public static boolean isAnyFileModified() {
    var anyFileModified = globalState().get(ANY_FILE_MODIFIED);
    return anyFileModified == null || ((boolean) anyFileModified);
  }

  public static CasSet getDoneCasSet() {
    return globalState().get(DONE_CAS_SET);
  }

  public static CasSet getPlannedCasSet() {
    return globalState().get(PLANNED_CAS_SET);
  }

  private static State globalState() {
    return AppContext.getDefault().globalState();
  }

  public static void addObserver(State.StateObserverCallbackRunnable observer) {
    getInstance().observers.add(observer);
  }

  public static void dispatch() {
    globalState().update(ANY_FILE_MODIFIED, true);
  }

  private static final PatriLangFilesWatcher INSTANCE = new PatriLangFilesWatcher();

  public static PatriLangFilesWatcher getInstance() {
    return INSTANCE;
  }
}
