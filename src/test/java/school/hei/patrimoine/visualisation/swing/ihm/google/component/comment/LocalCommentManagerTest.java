package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.*;

class LocalCommentManagerTest {
  private LocalCommentManager subject;
  private User testAuthor;

  private static final String FILE_ID = "file523";
  private static final String CONTENT = "Test du contenu";
  private static final String REPLY = "Test de la réponse";

  @BeforeEach
  void setUp() {
    subject = LocalCommentManager.getInstance();
    subject.clearAllPendingComments();

    testAuthor = User.builder().email("useremail@gmail.com").build();
  }

  @Test
  void add_and_get_pending_comment() {
    subject.addPendingComment(FILE_ID, CONTENT, testAuthor);

    List<PendingComment> pendingComments = subject.getPendingComments(FILE_ID);

    PendingComment pendingComment = pendingComments.getFirst();
    assertEquals(1, pendingComments.size());
    assertEquals(FILE_ID, pendingComment.fileId());
    assertEquals(CONTENT, pendingComment.content());
    assertEquals(testAuthor, pendingComment.author());
    assertNotNull(pendingComment.createdAt());
    assertEquals(SyncStatus.PENDING_ADD, pendingComment.status());
  }

  @Test
  void remove_pending_comment() {
    subject.addPendingComment(FILE_ID, CONTENT, testAuthor);
    PendingComment pendingComment = subject.getPendingComments(FILE_ID).getFirst();

    subject.removePendingComment(FILE_ID, pendingComment.localId());

    assertTrue(subject.getPendingComments(FILE_ID).isEmpty());
  }

  @Test
  void add_and_get_pending_reply() {
    subject.addPendingComment(FILE_ID, CONTENT, testAuthor);
    PendingComment pendingComment = subject.getPendingComments(FILE_ID).getFirst();

    subject.addPendingReply(FILE_ID, pendingComment.localId(), REPLY, testAuthor);

    PendingReply pendingReply = subject.getPendingReplies(FILE_ID).getFirst();

    assertEquals(1, subject.getPendingReplies(FILE_ID).size());
    assertEquals(FILE_ID, pendingReply.fileId());
    assertEquals(testAuthor, pendingReply.author());
    assertNotNull(pendingReply.createdAt());
    assertEquals(SyncStatus.PENDING_REPLY, pendingReply.status());
  }

  @Test
  void remove_pending_reply() {
    subject.addPendingComment(FILE_ID, CONTENT, testAuthor);
    PendingComment pendingComment = subject.getPendingComments(FILE_ID).getFirst();
    subject.addPendingReply(FILE_ID, pendingComment.localId(), REPLY, testAuthor);
    PendingReply pendingReply = subject.getPendingReplies(FILE_ID).getFirst();

    subject.removePendingReply(FILE_ID, pendingReply.localId());

    assertTrue(subject.getPendingReplies(FILE_ID).isEmpty());
  }

  @Test
  void add_and_get_pending_resolution() {
    subject.addPendingComment(FILE_ID, CONTENT, testAuthor);
    PendingComment pendingComment = subject.getPendingComments(FILE_ID).getFirst();

    subject.addPendingResolution(FILE_ID, pendingComment.localId());

    PendingResolution pendingResolution = subject.getPendingResolutions(FILE_ID).getFirst();
    assertEquals(1, subject.getPendingResolutions(FILE_ID).size());
    assertEquals(FILE_ID, pendingResolution.fileId());
    assertEquals(pendingComment.localId(), pendingResolution.commentId());
    assertNotNull(pendingResolution.resolvedAt());
    assertEquals(SyncStatus.PENDING_RESOLVE, pendingResolution.status());
  }

  @Test
  void remove_pending_resolution() {
    subject.addPendingComment(FILE_ID, CONTENT, testAuthor);
    PendingComment pendingComment = subject.getPendingComments(FILE_ID).getFirst();
    subject.addPendingResolution(FILE_ID, pendingComment.localId());
    PendingResolution pendingResolution = subject.getPendingResolutions(FILE_ID).getFirst();

    subject.removePendingResolution(FILE_ID, pendingResolution.commentId());

    assertTrue(subject.getPendingResolutions(FILE_ID).isEmpty());
  }

  @Test
  void add_and_get_pending_deletion() {
    subject.addPendingComment(FILE_ID, CONTENT, testAuthor);
    PendingComment pendingComment = subject.getPendingComments(FILE_ID).getFirst();

    subject.addPendingDeletion(FILE_ID, pendingComment.localId());

    PendingDeletion pendingDeletion = subject.getPendingDeletions(FILE_ID).getFirst();
    assertEquals(1, subject.getPendingDeletions(FILE_ID).size());
    assertEquals(FILE_ID, pendingDeletion.fileId());
    assertEquals(pendingComment.localId(), pendingDeletion.commentId());
    assertNotNull(pendingDeletion.deletedAt());
    assertEquals(SyncStatus.PENDING_DELETE, pendingDeletion.status());
  }

  @Test
  void remove_pending_deletion() {
    subject.addPendingComment(FILE_ID, CONTENT, testAuthor);
    PendingComment pendingComment = subject.getPendingComments(FILE_ID).getFirst();
    subject.addPendingDeletion(FILE_ID, pendingComment.localId());
    PendingDeletion pendingDeletion = subject.getPendingDeletions(FILE_ID).getFirst();

    subject.removePendingDeletion(FILE_ID, pendingDeletion.commentId());

    assertTrue(subject.getPendingDeletions(FILE_ID).isEmpty());
  }

  @Test
  void rempa_pending_actions() {
    String localCommentId = "local_abc123";
    String remoteCommentId = "remote_xyz789";

    subject.addPendingReply(FILE_ID, localCommentId, "Contenu de la réponse", testAuthor);
    subject.addPendingResolution(FILE_ID, localCommentId);
    subject.addPendingDeletion(FILE_ID, localCommentId);

    Map<String, String> mappings = Map.of(localCommentId, remoteCommentId);

    subject.remapPendingActions(FILE_ID, mappings);

    assertEquals(remoteCommentId, subject.getPendingReplies(FILE_ID).getFirst().parentCommentId());
    assertEquals(remoteCommentId, subject.getPendingResolutions(FILE_ID).getFirst().commentId());
    assertEquals(remoteCommentId, subject.getPendingDeletions(FILE_ID).getFirst().commentId());
  }

  @Test
  void testCleanUpCommentActions() {
    String commentId = "comment_123";
    subject.addPendingReply(FILE_ID, commentId, "Réponse 1", testAuthor);
    subject.addPendingReply(FILE_ID, commentId, "Réponse 2", testAuthor);
    subject.addPendingResolution(FILE_ID, commentId);

    subject.addPendingReply(FILE_ID, "other_comment", "Autre réponse", testAuthor);

    subject.cleanUpCommentActions(FILE_ID, commentId);

    List<PendingReply> replies = subject.getPendingReplies(FILE_ID);
    List<PendingResolution> resolutions = subject.getPendingResolutions(FILE_ID);

    assertEquals(1, replies.size());
    assertEquals("other_comment", replies.getFirst().parentCommentId());
    assertTrue(resolutions.isEmpty());
  }

  @Test
  void clear_all_pending_comments() {
    subject.addPendingComment(FILE_ID, "Comment", testAuthor);
    subject.addPendingReply(FILE_ID, "parent", "Reply", testAuthor);
    subject.addPendingResolution(FILE_ID, "comment");
    subject.addPendingDeletion(FILE_ID, "comment");

    subject.clearAllPendingComments();

    assertFalse(subject.hasAnyPendingComments());
    assertTrue(subject.getPendingComments(FILE_ID).isEmpty());
    assertTrue(subject.getPendingReplies(FILE_ID).isEmpty());
    assertTrue(subject.getPendingResolutions(FILE_ID).isEmpty());
    assertTrue(subject.getPendingDeletions(FILE_ID).isEmpty());
  }
}
