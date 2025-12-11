package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending;

public record PendingCommentsInfo(
    String fileId, int addCount, int replyCount, int resolveCount, int deleteCount) {}
