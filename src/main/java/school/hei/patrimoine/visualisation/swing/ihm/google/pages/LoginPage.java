package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;
import lombok.SneakyThrows;
import school.hei.patrimoine.google.api.DriveApi;
import school.hei.patrimoine.google.api.GoogleApi;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.PageManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.ButtonWithIcon;

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
    signInButton.addActionListener(onSigning());

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

  @SneakyThrows
  private ActionListener onSigning() {
    return e -> {
      var authDetails = googleApi.requestAuthentication();
      AppContext.getDefault().setData("auth-details", authDetails);
      AppContext.getDefault().setData("connected-user", authDetails.user());
      AppContext.getDefault().setData("drive-api", new DriveApi(authDetails));
      PageManager.navigateTo(SubmitLinkPage.PAGE_NAME);
    };
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
