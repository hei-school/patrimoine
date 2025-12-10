package school.hei.patrimoine.google.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class CommentTest {

  @Test
  void answer_contains_approuvé_in_lowercase_returns_true() {
    var user = new User("001", "Rakoto", "rakoto@gmail.com", "photo/rakoto", true);
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
  void answer_contains_approuvé_in_uppercase_returns_true() {
    var user = new User("001", "Rakoto", "rakoto@gmail.com", "photo/rakoto", true);
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
  void answer_contains_approuvé_with_mixed_case_returns_true() {
    var user = new User("001", "Rakoto", "rakoto@gmail.com", "photo/rakoto", true);
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
  void answer_contains_approuvé_within_sentence_returns_false() {
    var user = new User("001", "Rakoto", "rakoto@gmail.com", "photo/rakoto", true);
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
  void answer_null_returns_false() {
    var user = new User("001", "Rakoto", "rakoto@gmail.com", "photo/rakoto", true);

    var subject =
        new Comment("0001", "Afficher les commentaires en vert", Instant.now(), user, false, null);

    assertFalse(subject.isApproved());
  }
}
