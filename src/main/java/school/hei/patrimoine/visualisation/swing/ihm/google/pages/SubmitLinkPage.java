package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import static java.awt.Font.BOLD;
import static java.awt.Font.PLAIN;
import static school.hei.patrimoine.google.DriveLinkIdParser.GOOGLE_DRIVE_ID_PATTERN;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.swing.*;

import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.PageManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedLink;

public class SubmitLinkPage extends Page {
    public static final String PAGE_NAME = "submit-file-url";
    private final JTextArea plannedInput;
    private final JTextArea doneInput;

    public SubmitLinkPage() {
        super(PAGE_NAME);
        this.plannedInput = new JTextArea();
        this.doneInput = new JTextArea();

        setLayout(new BorderLayout());

        addTitle();
        addInputs();
        addSubmitButton();
    }

    private void addTitle() {
        var title = new JLabel("Saisir les liens Google (Prévu et Réalisé) :");
        title.setFont(new Font("Arial", BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        add(title, BorderLayout.NORTH);
    }

    private void addInputs() {
        var formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 20, 50));

        var plannedLabel = new JLabel("Liens Prévu :");
        plannedLabel.setFont(new Font("Arial", Font.BOLD, 16));
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

        var doneLabel = new JLabel("Liens Réalisé :");
        doneLabel.setFont(new Font("Arial", Font.BOLD, 16));
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

        var mainScroll = new JScrollPane(formPanel);
        mainScroll.setBorder(BorderFactory.createEmptyBorder());

        add(mainScroll, BorderLayout.CENTER);
    }

    private void addSubmitButton() {
        var submitButton = new Button("Envoyer");
        submitButton.setFont(new Font("Arial", BOLD, 18));
        submitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        submitButton.setPreferredSize(new Dimension(200, 50));
        submitButton.addActionListener(e -> loadDataInBackground());

        var wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));
        wrapper.add(submitButton);

        add(wrapper, BorderLayout.SOUTH);
    }

    private void loadDataInBackground() {
        SwingWorker<GoogleLinkList<NamedLink>, Void> worker =
                new SwingWorker<>() {
                    @Override
                    protected GoogleLinkList<NamedLink> doInBackground() {
                        var plannedLinks = extractDriveLinks(plannedInput.getText());
                        var doneLinks = extractDriveLinks(doneInput.getText());

                        return new GoogleLinkList<>(plannedLinks, doneLinks);
                    }

                    @Override
                    protected void done() {
                        try{
                            var links = get();
                            AppContext.getDefault().setData("named-links", links);
                            PageManager.navigateTo(LinkValidityPage.PAGE_NAME);
                        }catch (Exception e){
                            throw new RuntimeException(e);
                        }
                    }
                };

        worker.execute();
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

    private List<NamedLink> extractDriveLinks(String rawText) {
        var lines = Arrays.asList(rawText.split("\n"));

        return lines.stream()
                .filter(line -> GOOGLE_DRIVE_ID_PATTERN.matcher(line).find())
                .map(this::extractDriveLink)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
