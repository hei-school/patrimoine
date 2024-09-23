package school.hei.patrimoine.visualisation.swing.ihm;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import lombok.SneakyThrows;
import school.hei.patrimoine.visualisation.utils.GoogleApi;
import school.hei.patrimoine.visualisation.utils.GoogleApi.GoogleAuthenticationDetails;

public class GoogleAuthScreen extends JFrame {

  private final GoogleApi googleApi;

  /**
   * Global instance of the scopes required by this quickstart. If modifying these scopes, delete
   * your previously saved tokens/ folder.
   */
  public GoogleAuthScreen() {
    JButton signInButton = new JButton("Sign in with Google");
    signInButton.addActionListener(onSignin());

    setLayout(new BorderLayout());
    add(signInButton, CENTER);

    setSize(400, 200);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
    googleApi = new GoogleApi();
  }

  private ActionListener onSignin() {
    return e -> {
      var authReqRes = handleGoogleSignIn();
      invokeLater(() -> new GoogleDocsSubmitScreen(googleApi, authReqRes));
      setVisible(false);
    };
  }

  @SneakyThrows
  private GoogleAuthenticationDetails handleGoogleSignIn() {
    return googleApi.requestAuthentication();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(GoogleAuthScreen::new);
  }
}
