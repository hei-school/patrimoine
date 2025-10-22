package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showError;

import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;
import school.hei.patrimoine.google.api.CommentApi;
import school.hei.patrimoine.google.api.DriveApi;
import school.hei.patrimoine.google.api.GoogleApi;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.ButtonWithIcon;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;

public class LoginPage extends Page {
  private final GoogleApi googleApi;
  public static final String PAGE_NAME = "login";

  public LoginPage() {
    super(PAGE_NAME);
    googleApi = new GoogleApi();

    setLayout(new BorderLayout());
    var title = new JLabel("Patrimoine");
    title.setFont(new Font("Arial", Font.BOLD, 32));

    var signInButton = new ButtonWithIcon("Se connecter avec Google", loadGoogleLogo());
    signInButton.addActionListener(e -> onSigning());

    var centerPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets = new Insets(4, 0, 4, 0);

    gbc.gridy = 0;
    centerPanel.add(title, gbc);

    gbc.gridy = 1;
    centerPanel.add(signInButton, gbc);

    add(centerPanel, BorderLayout.CENTER);
  }

  private void onSigning() {
    AsyncTask.<GoogleApi.AuthDetails>builder()
        .task(googleApi::requestAuthentication)
        .onSuccess(
            (authDetails) -> {
              var driveApi = new DriveApi(authDetails);
              globalState()
                  .update(
                      Map.of(
                          "auth-details",
                          authDetails,
                          "connected-user",
                          authDetails.user(),
                          "drive-api",
                          driveApi,
                          "comment-api",
                          new CommentApi(driveApi)));
              pageManager().navigate(SubmitLinkPage.PAGE_NAME);
            })
        .onError(e -> showError("Erreur", "Erreur d'authentification, veuillez réessayer"))
        .build()
        .execute();
  }

  @Override
  protected boolean destroyOnLeave() {
    return true;
  }

  private Image loadGoogleLogo() {
    try {
      var googleLogo =
          ImageIO.read(Objects.requireNonNull(getClass().getResource("/google_logo.png")));
      return googleLogo.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
