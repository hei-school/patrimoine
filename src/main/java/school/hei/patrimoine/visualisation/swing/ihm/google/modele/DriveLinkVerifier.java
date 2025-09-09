package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import static school.hei.patrimoine.google.DriveLinkIdParser.GOOGLE_DRIVE_ID_PATTERN;

public class DriveLinkVerifier implements LinkVerifier {
  @Override
  public boolean verify(String link) {
    return GOOGLE_DRIVE_ID_PATTERN.matcher(link).find();
  }
}
