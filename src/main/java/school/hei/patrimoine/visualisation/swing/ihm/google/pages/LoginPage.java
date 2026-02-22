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
import school.hei.patrimoine.visualisation.swing.ihm.google.component.CustomBorder;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.ButtonWithIcon;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;

public class LoginPage extends Page {
  private final GoogleApi googleApi;
  public static final String PAGE_NAME = "login";
  private static final Color BASE_COLOR = new Color(28, 63, 90);
  private static final Color BACKGROUND_START = new Color(244, 252, 255);
  private static final Color BACKGROUND_END = new Color(255, 237, 191);
  private static final Color BORDER_COLOR = new Color(180, 180, 180);
  private static final Color FONT_COLOR = new Color(121, 121, 121);

  public LoginPage() {
    super(PAGE_NAME);
    googleApi = new GoogleApi();

    setLayout(new BorderLayout());

    var backgroundPanel =
        new JPanel(new BorderLayout()) {
          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            var gradient =
                new GradientPaint(0, 0, BACKGROUND_START, 0, getHeight(), BACKGROUND_END);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
          }
        };
    backgroundPanel.setOpaque(true);
    add(backgroundPanel, BorderLayout.CENTER);

    var title = new JLabel("Patrimoine");
    title.setFont(new Font("Arial", Font.BOLD, 50));
    title.setForeground(BASE_COLOR);

    var intro =
        new JLabel(
            "<html><center>Prenez le contrôle de votre patrimoine avec <br>une vision claire et"
                + " évolutive.</center></html>");
    intro.setFont(new Font("Arial", Font.PLAIN, 20));
    intro.setForeground(FONT_COLOR);

    var descPanel = new JPanel();
    descPanel.setOpaque(false);
    descPanel.setLayout(new GridLayout(3, 1, 15, 20));

    descPanel.add(
        createListItem("Déclarez ce que vous possédez", loadIcon("/icons/possession.png")));
    descPanel.add(
        createListItem(
            "Projetez votre situation financière dans le futur",
            loadIcon("/icons/projection.png")));
    descPanel.add(
        createListItem(
            "Anticipez les risques et prenez de meilleures décisions",
            loadIcon("/icons/risk.png")));

    var signInButton =
        new ButtonWithIcon(
            "Se connecter avec Google", loadGoogleLogo(), BASE_COLOR, Color.WHITE, 20);
    signInButton.addActionListener(e -> onSigning());

    var centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets = new Insets(4, 0, 4, 0);

    gbc.gridy = 0;
    centerPanel.add(title, gbc);

    gbc.gridy = 1;
    centerPanel.add(intro, gbc);

    gbc.gridy = 2;
    gbc.insets = new Insets(30, 0, 0, 0);
    centerPanel.add(descPanel, gbc);

    gbc.gridy = 3;
    gbc.insets = new Insets(80, 0, 0, 0);
    centerPanel.add(signInButton, gbc);

    backgroundPanel.add(centerPanel, BorderLayout.CENTER);
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

  private JPanel createListItem(String text, ImageIcon icon) {
    var itemPanel = new JPanel();

    itemPanel.setOpaque(false);
    itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
    itemPanel.setPreferredSize(new Dimension(530, 85));
    itemPanel.setMaximumSize(new Dimension(530, 85));
    itemPanel.setMinimumSize(new Dimension(530, 85));

    itemPanel.setBorder(
        CustomBorder.builder()
            .borderColor(LoginPage.BORDER_COLOR)
            .thickness(1)
            .radius(20)
            .padding(5, 20)
            .build());

    var iconLabel = new JLabel(icon);
    var textLabel = new JLabel(text);
    textLabel.setFont(new Font("Arial", Font.BOLD, 16));
    textLabel.setForeground(Color.BLACK);

    itemPanel.add(iconLabel);
    itemPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    itemPanel.add(textLabel);

    return itemPanel;
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

  private ImageIcon loadIcon(String path) {
    var url = getClass().getResource(path);
    var icon = new ImageIcon(Objects.requireNonNull(url));
    Image scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

    return new ImageIcon(scaled);
  }
}
