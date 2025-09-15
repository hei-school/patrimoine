package school.hei.patrimoine.google;

import static school.hei.patrimoine.google.DriveLinkIdParser.GOOGLE_DRIVE_ID_PATTERN;

public record DriveLinkVerifier() {
  public boolean verify(String link) {
    return GOOGLE_DRIVE_ID_PATTERN.matcher(link).find();
  }
}
