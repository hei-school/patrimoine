package school.hei.patrimoine.visualisation.swing.ihm.google;

import static javax.swing.SwingUtilities.invokeLater;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;
import lombok.SneakyThrows;
import school.hei.patrimoine.google.GoogleApi;
import school.hei.patrimoine.google.GoogleApi.GoogleAuthenticationDetails;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.ButtonWithIcon;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Screen;

public class GoogleAuthScreen extends Screen {
  private final GoogleApi googleApi;

  /**
   * Global instance of the scopes required by this quickstart. If modifying these scopes, delete
   * your previously saved tokens/ folder.
   */
  public GoogleAuthScreen() {
    super("Authentification Google", 700, 400);
    googleApi = new GoogleApi();

    setLayout(new BorderLayout());

    var title = new JLabel("Patrimoine");
    title.setFont(new Font("Arial", Font.BOLD, 32));
    title.setForeground(new Color(66, 66, 66)); // Gris foncé

    var signInButton = new ButtonWithIcon("Se connecter avec Google", loadGoogleLogo());
    signInButton.addActionListener(onSignin());

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

  private ActionListener onSignin() {
    return e -> {
      var authReqRes = handleGoogleSignIn();
      invokeLater(() -> new GoogleSubmitScreen(googleApi, authReqRes));
      dispose();
    };
  }

  private Image loadGoogleLogo() {
    try {
       var googleLogo = ImageIO.read(Objects.requireNonNull(getClass().getResource("/google_logo.png")));
      return googleLogo.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @SneakyThrows
  private GoogleAuthenticationDetails handleGoogleSignIn() {
    return googleApi.requestAuthentication();
  }

  public static void main(String[] args) {
    System.setProperty("awt.useSystemAAFontSettings", "on");
    System.setProperty("swing.aatext", "true");
    SwingUtilities.invokeLater(GoogleAuthScreen::new);
  }
}
