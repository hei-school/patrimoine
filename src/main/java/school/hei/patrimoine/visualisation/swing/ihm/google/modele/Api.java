package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import school.hei.patrimoine.google.api.CommentApi;
import school.hei.patrimoine.google.api.DriveApi;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;

public class Api {
  public static CommentApi commentApi() {
    return AppContext.getDefault().getData("comment-api");
  }

  public static DriveApi driveApi() {
    return AppContext.getDefault().getData("drive-api");
  }
}
