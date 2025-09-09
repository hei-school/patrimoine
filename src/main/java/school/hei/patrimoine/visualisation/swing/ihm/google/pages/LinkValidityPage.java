package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import static java.awt.Font.BOLD;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.google.DriveLinkIdParser;
import school.hei.patrimoine.google.api.DriveApi;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.PageManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.NavigateButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.downloader.DriveNamedIdDownloader;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.*;

@Slf4j
public class LinkValidityPage extends Page {
  public static final String PAGE_NAME = "link-validity";

  private final DefaultListModel<NamedLink> doneNamedLinksModel;
  private final DefaultListModel<NamedLink> plannedNamedLinksModel;

  private final JList<NamedLink> plannedNamedLinksList;
  private final JList<NamedLink> doneNamedLinksList;

  public LinkValidityPage() {
    super(LinkValidityPage.PAGE_NAME);

    this.plannedNamedLinksModel = new DefaultListModel<>();
    this.doneNamedLinksModel = new DefaultListModel<>();

    this.plannedNamedLinksList = new JList<>(plannedNamedLinksModel);
    this.doneNamedLinksList = new JList<>(doneNamedLinksModel);

    this.plannedNamedLinksList.setCellRenderer(new NameLinkRenderer());
    this.doneNamedLinksList.setCellRenderer(new NameLinkRenderer());

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

    addTitle();
    addNamedLinksLists();
    addSubmitButton();

    subscribe("named-links");
  }

  private void addTitle() {
    var title = new JLabel("Soumettre vos liens Google");
    title.setFont(new Font("Arial", BOLD, 24));
    title.setHorizontalAlignment(SwingConstants.CENTER);
    title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

    add(title, BorderLayout.NORTH);
  }

  private void addNamedLinksLists() {
    var panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

    var plannedLabel = new JLabel("Liens vers les journaux planifiés :");
    plannedLabel.setFont(new Font("Arial", BOLD, 18));
    plannedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(plannedLabel);

    var plannedScroll = new JScrollPane(plannedNamedLinksList);
    plannedScroll.setPreferredSize(new Dimension(400, 150));
    plannedScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(plannedScroll);

    panel.add(Box.createVerticalStrut(20));

    var doneLabel = new JLabel("Liens vers les journaux réalisés :");
    doneLabel.setFont(new Font("Arial", BOLD, 18));
    doneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(doneLabel);

    var doneScroll = new JScrollPane(doneNamedLinksList);
    doneScroll.setPreferredSize(new Dimension(400, 150));
    doneScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(doneScroll);

    add(panel, BorderLayout.CENTER);
  }

  private void addSubmitButton() {
    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
    buttonPanel.add(returnButton());
    buttonPanel.add(submitButton());

    add(buttonPanel, BorderLayout.SOUTH);
  }

  private Button submitButton() {
    var submitButton = new Button("Envoyer");
    submitButton.setPreferredSize(new Dimension(200, 50));
    submitButton.setFont(new Font("Arial", BOLD, 18));
    submitButton.addActionListener(e -> downloadFilesInBackground());

    return submitButton;
  }

  private Button returnButton() {
    var returnButton = new NavigateButton("Retour", SubmitLinkPage.PAGE_NAME);
    returnButton.setPreferredSize(new Dimension(200, 50));
    returnButton.setFont(new Font("Arial", BOLD, 18));

    return returnButton;
  }

  @Override
  public void update(AppContext appContext) {
    GoogleLinkList<NamedLink> links = appContext.getData("named-links");

    plannedNamedLinksModel.clear();
    doneNamedLinksModel.clear();

    links.planned().forEach(plannedNamedLinksModel::addElement);
    links.done().forEach(doneNamedLinksModel::addElement);
  }

  private GoogleLinkList<NamedID> parseNamedIds() {
    GoogleLinkList<NamedLink> namedLinks = AppContext.getDefault().getData("named-links");

    List<NamedID> plannedIds = new ArrayList<>();
    List<NamedID> doneIds = new ArrayList<>();
    DriveLinkIdParser idParser = new DriveLinkIdParser();

    for (var namedLink : namedLinks.done()) {
      var parsedId = idParser.apply(namedLink.link());
      plannedIds.add(new NamedID(namedLink.name(), parsedId));
    }

    for (var namedLink : namedLinks.planned()) {
      var parsedId = idParser.apply(namedLink.link());
      doneIds.add(new NamedID(namedLink.name(), parsedId));
    }

    return new GoogleLinkList<>(plannedIds, doneIds);
  }

  private void downloadFilesInBackground() {
    var loadingDialog = new Dialog("Traitement...", 300, 100);

    SwingWorker<Void, Void> worker =
        new SwingWorker<>() {
          @Override
          protected Void doInBackground() throws GoogleIntegrationException {
            var namedIds = parseNamedIds();
            DriveApi driveApi = AppContext.getDefault().getData("drive-api");
            var downloader = new DriveNamedIdDownloader(driveApi);

            // downloader.apply(namedIds);

            AppContext.getDefault().setData("named-ids", namedIds);

            return null;
          }

          @Override
          protected void done() {
            loadingDialog.dispose();
            try {
              PageManager.navigateTo(PatriLangFilesPage.PAGE_NAME);
            } catch (Exception e) {
              log.info(e.getMessage());

              showMessageDialog(
                  AppContext.getDefault().app(),
                  "Veuillez vérifier le contenu de vos documents",
                  "Erreur",
                  JOptionPane.ERROR_MESSAGE);
            }
          }
        };

    worker.execute();
    loadingDialog.setVisible(true);
  }

  private static class NameLinkRenderer extends JPanel implements ListCellRenderer<NamedLink> {
    private final JLabel nameLabel;
    private final JLabel valueLabel;
    private final DriveLinkVerifier driveLinkVerifier;

    public NameLinkRenderer() {
      this.driveLinkVerifier = new DriveLinkVerifier();

      setLayout(new BorderLayout(10, 5));
      setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      this.nameLabel = new JLabel();
      this.nameLabel.setFont(new Font("Arial", BOLD, 16));

      this.valueLabel = new JLabel();
      this.valueLabel.setFont(new Font("Arial", Font.PLAIN, 16));

      add(nameLabel, BorderLayout.WEST);
      add(valueLabel, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(
        JList<? extends NamedLink> list,
        NamedLink value,
        int index,
        boolean isSelected,
        boolean cellHasFocus) {

      nameLabel.setText(value.name());
      valueLabel.setText(value.link());

      if (driveLinkVerifier.verify(value.link())) {
        setBackground(Color.GREEN);
      } else {
        setBackground(Color.RED);
      }

      return this;
    }
  }
}
