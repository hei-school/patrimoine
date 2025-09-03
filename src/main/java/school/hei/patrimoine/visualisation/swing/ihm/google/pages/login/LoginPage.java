package school.hei.patrimoine.visualisation.swing.ihm.google.pages.login;

import lombok.SneakyThrows;
import school.hei.patrimoine.google.api.GoogleApi;
import school.hei.patrimoine.visualisation.swing.ihm.google.GoogleSubmitApp;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.ButtonWithIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

import static javax.swing.SwingUtilities.invokeLater;

public class LoginPage extends Page {
    private final GoogleApi googleApi;

    /**
     * Global instance of the scopes required by this quickstart. If modifying these scopes, delete
     * your previously saved tokens/ folder.
     */
    public LoginPage() {
        super("login");
        googleApi = new GoogleApi();

        setLayout(new BorderLayout());

        var title = new JLabel("Patrimoine");
        title.setFont(new Font("Arial", Font.BOLD, 32));

        var signInButton = new ButtonWithIcon("Se connecter avec Google", loadGoogleLogo());

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

    private ActionListener onSigning() {
        return e -> {
            var authDetails = handleGoogleSignIn();
            invokeLater(() -> new GoogleSubmitApp(authDetails));
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

    @SneakyThrows
    private GoogleApi.AuthDetails handleGoogleSignIn() {
        return googleApi.requestAuthentication();
    }
}
