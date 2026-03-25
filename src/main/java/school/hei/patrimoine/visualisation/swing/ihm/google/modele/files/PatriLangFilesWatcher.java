package school.hei.patrimoine.visualisation.swing.ihm.google.modele.files;

import static org.reflections.Reflections.log;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getDoneCasSetFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getPlannedCasSetFile;

import java.util.*;
import javax.swing.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.possession.Compte;
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

    CasSet done = null;
    CasSet planned = null;

    try {
      done = transpileToutCas(getDoneCasSetFile());
    } catch (ParseCancellationException e) {
      log.warn("Fichier done ignoré au démarrage : {}", e.getMessage());
      SwingUtilities.invokeLater(
          () ->
              JOptionPane.showMessageDialog(
                  null, e.getMessage(), "Erreur de syntaxe", JOptionPane.WARNING_MESSAGE));
    }

    try {
      planned = transpileToutCas(getPlannedCasSetFile());
    } catch (ParseCancellationException e) {
      log.warn("Fichier planned ignoré au démarrage : {}", e.getMessage());
      SwingUtilities.invokeLater(
          () ->
              JOptionPane.showMessageDialog(
                  null, e.getMessage(), "Erreur de syntaxe", JOptionPane.WARNING_MESSAGE));
    }

    var updates = new HashMap<String, Object>();
    if (done != null) {
      updates.put(DONE_CAS_SET, done);
      updates.put(DONE_CAS_SET_COMPTES, CompteGetter.getComptes(done));
    }
    if (planned != null) updates.put(PLANNED_CAS_SET, planned);

    if (!updates.isEmpty()) {
      globalState().update(updates);
    }
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

  public static Set<Compte> getDoneCasSetComptes() {
    return globalState().get(DONE_CAS_SET_COMPTES);
  }

  public static Cas getCas(PatriLangFileContext file) {
    var casSet = getCasSet(file);
    if (casSet == null) return null;

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
