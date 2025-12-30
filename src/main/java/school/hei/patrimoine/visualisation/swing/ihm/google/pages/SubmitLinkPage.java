package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import static java.awt.Font.BOLD;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList.*;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.google.DriveLinkVerifier;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkListCacheManager;

@Slf4j
public class SubmitLinkPage extends Page {
  public static final String PAGE_NAME = "submit-file-url";
  private final JTextArea doneInput;
  private final JTextArea plannedInput;
  private final JTextArea justificativeInput;
  private final DriveLinkVerifier linkVerifier;

  public SubmitLinkPage() {
    super(PAGE_NAME);
    this.doneInput = new JTextArea();
    this.plannedInput = new JTextArea();
    this.justificativeInput = new JTextArea();
    this.linkVerifier = new DriveLinkVerifier();

    setLayout(new BorderLayout());
    addTitle();
    addInputs();
    loadExistingLinks();
    addSubmitButton();
  }

  private void addTitle() {
    var title = new JLabel("Saisir les liens Google Drive des journaux :");
    title.setFont(new Font("Arial", BOLD, 24));
    title.setHorizontalAlignment(SwingConstants.CENTER);
    title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

    add(title, BorderLayout.NORTH);
  }

  private void addInputs() {
    var formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 20, 50));

    var plannedLabel = new JLabel("Liens vers les journaux planifiés :");
    plannedLabel.setFont(new Font("Arial", Font.BOLD, 18));
    plannedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    formPanel.add(plannedLabel);

    plannedInput.setLineWrap(true);
    plannedInput.setWrapStyleWord(true);
    plannedInput.setFont(new Font("Arial", Font.PLAIN, 16));

    var plannedScroll = new JScrollPane(plannedInput);
    plannedScroll.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));
    plannedScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
    plannedScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
    formPanel.add(plannedScroll);

    var doneLabel = new JLabel("Liens vers les journaux réalisés :");
    doneLabel.setFont(new Font("Arial", Font.BOLD, 18));
    doneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    formPanel.add(doneLabel);

    doneInput.setLineWrap(true);
    doneInput.setWrapStyleWord(true);
    doneInput.setFont(new Font("Arial", Font.PLAIN, 16));

    var doneScroll = new JScrollPane(doneInput);
    doneScroll.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));
    doneScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
    doneScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
    formPanel.add(doneScroll);

    var justificativeLabel = new JLabel("Liens vers les pièces justificatives :");
    justificativeLabel.setFont(new Font("Arial", Font.BOLD, 18));
    justificativeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    formPanel.add(justificativeLabel);

    justificativeInput.setLineWrap(true);
    justificativeInput.setWrapStyleWord(true);
    justificativeInput.setFont(new Font("Arial", Font.PLAIN, 16));

    var justificativeScroll = new JScrollPane(justificativeInput);
    justificativeScroll.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));
    justificativeScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
    justificativeScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
    formPanel.add(justificativeScroll);

    var mainScroll = new JScrollPane(formPanel);
    mainScroll.setBorder(BorderFactory.createEmptyBorder());

    add(mainScroll, BorderLayout.CENTER);
  }

  private void addSubmitButton() {
    var submitButton = new Button("Envoyer");
    submitButton.setFont(new Font("Arial", BOLD, 18));
    submitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
    submitButton.setPreferredSize(new Dimension(200, 50));
    submitButton.addActionListener(e -> submitLink());

    var wrapper = new JPanel();
    wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
    wrapper.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));
    wrapper.add(submitButton);

    add(wrapper, BorderLayout.SOUTH);
  }

  private void loadExistingLinks() {
    var saver = new GoogleLinkListCacheManager();
    plannedInput.setText(saver.loadPlannedLinks());
    doneInput.setText(saver.loadDoneLinks());
    justificativeInput.setText(saver.loadJustificativeLinks());
  }

  private void submitLink() {
    AsyncTask.<GoogleLinkList<NamedLink>>builder()
        .task(
            () -> {
              var plannedLinks = extractDriveLinks(plannedInput.getText());
              var doneLinks = extractDriveLinks(doneInput.getText());
              var justificativeLinks = extractDriveLinks(justificativeInput.getText());
              return new GoogleLinkList<>(plannedLinks, doneLinks, justificativeLinks);
            })
        .onSuccess(
            links -> {
              globalState().update("named-links", links);
              pageManager().navigate(LinkValidityPage.PAGE_NAME);
            })
        .withDialogLoading(false)
        .build()
        .execute();
  }

  private List<NamedLink> extractDriveLinks(String rawText) {
    var lines = Arrays.asList(rawText.split("\n"));
    return lines.stream()
        .filter(linkVerifier::verify)
        .map(this::extractDriveLink)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private Optional<NamedLink> extractDriveLink(String line) {
    var parts = line.split(":", 2);

    if (parts.length != 2) {
      return Optional.empty();
    }

    var linkName = parts[0].trim();
    var linkValue = parts[1].trim();
    return Optional.of(new NamedLink(linkName, linkValue));
  }
}
