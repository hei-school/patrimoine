package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending;

import lombok.Builder;

@Builder
public record PendingCommentsInfo(
    String fileId, int addCount, int replyCount, int resolveCount, int deleteCount) {}
