package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

public interface AppContextObserver {
  void update(AppContext appContext);

  void subscribe(String contextId, String dataKey);

  void subscribe(String dataKey);
}
