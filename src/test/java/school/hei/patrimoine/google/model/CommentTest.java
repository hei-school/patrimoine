package school.hei.patrimoine.google.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class CommentTest {

  @Test
  void réponse_contient_approuvé_en_minuscule_retourne_true() {
    var user = new User("001", "Rakoto", "rakoto@gmail.com", "photo/rakoto");
    var answer = new Comment("005", "approuvé", Instant.now(), user, false, null);

    var subject =
        new Comment(
            "0001",
            "Afficher les commentaires en vert",
            Instant.now(),
            user,
            false,
            List.of(answer));

    assertTrue(subject.isApproved());
  }

  @Test
  void réponse_contient_approuvé_en_majuscule_retourne_true() {
    var user = new User("001", "Rakoto", "rakoto@gmail.com", "photo/rakoto");
    var answer = new Comment("005", "APPROUVÉ", Instant.now(), user, false, null);

    var subject =
        new Comment(
            "0001",
            "Afficher les commentaires en vert",
            Instant.now(),
            user,
            false,
            List.of(answer));

    assertTrue(subject.isApproved());
  }

  @Test
  void réponse_contient_approuvé_avec_casse_mixte_retourne_true() {
    var user = new User("001", "Rakoto", "rakoto@gmail.com", "photo/rakoto");
    var answer = new Comment("005", "aPProuVÉ", Instant.now(), user, false, null);

    var subject =
        new Comment(
            "0001",
            "Afficher les commentaires en vert",
            Instant.now(),
            user,
            false,
            List.of(answer));

    assertTrue(subject.isApproved());
  }

  @Test
  void réponse_contient_approuvé_dans_une_phrase_retourne_false() {
    var user = new User("001", "Rakoto", "rakoto@gmail.com", "photo/rakoto");
    var answer = new Comment("005", "Commentaire approuvé", Instant.now(), user, false, null);

    var subject =
        new Comment(
            "0001",
            "Afficher les commentaires en vert",
            Instant.now(),
            user,
            false,
            List.of(answer));

    assertFalse(subject.isApproved());
  }

  @Test
  void réponse_null_retourne_false() {
    var user = new User("001", "Rakoto", "rakoto@gmail.com", "photo/rakoto");

    var subject =
        new Comment("0001", "Afficher les commentaires en vert", Instant.now(), user, false, null);

    assertFalse(subject.isApproved());
  }
}
