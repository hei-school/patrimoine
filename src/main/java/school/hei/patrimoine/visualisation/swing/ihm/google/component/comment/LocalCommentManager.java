package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.*;

public class LocalCommentManager {
  private static LocalCommentManager INSTANCE;

  private final Map<String, List<PendingComment>> pendingCommentsByFile;
  private final Map<String, List<PendingReply>> pendingRepliesByFile;
  private final Map<String, List<PendingResolution>> pendingResolutionsByFile;
  private final Map<String, List<PendingDeletion>> pendingDeletionsByFile;
  private final Map<String, Map<String, String>> idMappingsByFile = new ConcurrentHashMap<>();

  private LocalCommentManager() {
    this.pendingCommentsByFile = new ConcurrentHashMap<>();
    this.pendingRepliesByFile = new ConcurrentHashMap<>();
    this.pendingResolutionsByFile = new ConcurrentHashMap<>();
    this.pendingDeletionsByFile = new ConcurrentHashMap<>();
  }

  public static LocalCommentManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new LocalCommentManager();
    }
    return INSTANCE;
  }

  public boolean hasAnyPendingComments() {
    return !pendingCommentsByFile.isEmpty()
        || !pendingRepliesByFile.isEmpty()
        || !pendingResolutionsByFile.isEmpty()
        || !pendingDeletionsByFile.isEmpty();
  }

  public void addPendingComment(String fileId, String content, User author) {
    String localId = generateLocalId();
    var pendingComment =
        new PendingComment(localId, fileId, content, Instant.now(), author, SyncStatus.PENDING_ADD);

    pendingCommentsByFile.computeIfAbsent(fileId, k -> new ArrayList<>()).add(pendingComment);
  }

  public List<PendingComment> getPendingComments(String fileId) {
    return pendingCommentsByFile.getOrDefault(fileId, new ArrayList<>());
  }

  public void removePendingComment(String fileId, String localId) {
    List<PendingComment> comments = pendingCommentsByFile.get(fileId);
    if (comments == null) return;

    comments.removeIf(comment -> comment.localId().equals(localId));
  }

  public void addPendingReply(String fileId, String parentCommentId, String content, User author) {
    String localId = generateLocalId();
    var pendingReply =
        new PendingReply(
            localId,
            fileId,
            parentCommentId,
            content,
            Instant.now(),
            author,
            SyncStatus.PENDING_REPLY);

    pendingRepliesByFile.computeIfAbsent(fileId, k -> new ArrayList<>()).add(pendingReply);
  }

  public List<PendingReply> getPendingReplies(String fileId) {
    return pendingRepliesByFile.getOrDefault(fileId, new ArrayList<>());
  }

  public void removePendingReply(String fileId, String localId) {
    List<PendingReply> replies = pendingRepliesByFile.get(fileId);
    if (replies == null) return;

    replies.removeIf(reply -> reply.localId().equals(localId));
  }

  public void addPendingResolution(String fileId, String commentId) {
    var pendingResolution =
        new PendingResolution(fileId, commentId, Instant.now(), SyncStatus.PENDING_RESOLVE);

    pendingResolutionsByFile.computeIfAbsent(fileId, k -> new ArrayList<>()).add(pendingResolution);
  }

  public List<PendingResolution> getPendingResolutions(String fileId) {
    return pendingResolutionsByFile.getOrDefault(fileId, new ArrayList<>());
  }

  public void removePendingResolution(String fileId, String commentId) {
    List<PendingResolution> resolutions = pendingResolutionsByFile.get(fileId);
    if (resolutions == null) return;

    resolutions.removeIf(resolution -> resolution.commentId().equals(commentId));
  }

  public void addPendingDeletion(String fileId, String commentId) {
    var pendingDeletion =
        new PendingDeletion(fileId, commentId, Instant.now(), SyncStatus.PENDING_DELETE);

    pendingDeletionsByFile.computeIfAbsent(fileId, k -> new ArrayList<>()).add(pendingDeletion);
  }

  public List<PendingDeletion> getPendingDeletions(String fileId) {
    return pendingDeletionsByFile.getOrDefault(fileId, new ArrayList<>());
  }

  public void removePendingDeletion(String fileId, String commentId) {
    List<PendingDeletion> deletions = pendingDeletionsByFile.get(fileId);
    if (deletions == null) return;

    deletions.removeIf(deletion -> deletion.commentId().equals(commentId));
  }

  public void remapPendingActions(String fileId, Map<String, String> newMappings) {
    Map<String, String> fileMappings =
        idMappingsByFile.computeIfAbsent(fileId, k -> new ConcurrentHashMap<>());
    fileMappings.putAll(newMappings);

    List<PendingReply> replies = pendingRepliesByFile.get(fileId);
    if (replies != null && !replies.isEmpty()) {
      List<PendingReply> remappedReplies = new ArrayList<>();
      for (PendingReply reply : replies) {
        if (reply.parentCommentId().startsWith("local_")) {
          String remoteId = fileMappings.get(reply.parentCommentId());
          if (remoteId != null) {
            PendingReply remapped =
                new PendingReply(
                    reply.localId(),
                    reply.fileId(),
                    remoteId,
                    reply.content(),
                    reply.createdAt(),
                    reply.author(),
                    reply.status());
            remappedReplies.add(remapped);
          } else {
            remappedReplies.add(reply);
          }
        } else {
          remappedReplies.add(reply);
        }
      }
      pendingRepliesByFile.put(fileId, remappedReplies);
    }

    List<PendingResolution> resolutions = pendingResolutionsByFile.get(fileId);
    if (resolutions != null && !resolutions.isEmpty()) {
      List<PendingResolution> remappedResolutions = new ArrayList<>();
      for (PendingResolution resolution : resolutions) {
        if (resolution.commentId().startsWith("local_")) {
          String remoteId = fileMappings.get(resolution.commentId());
          if (remoteId != null) {
            PendingResolution remapped =
                new PendingResolution(
                    resolution.fileId(), remoteId, resolution.resolvedAt(), resolution.status());
            remappedResolutions.add(remapped);
          } else {
            remappedResolutions.add(resolution);
          }
        } else {
          remappedResolutions.add(resolution);
        }
      }
      pendingResolutionsByFile.put(fileId, remappedResolutions);
    }

    List<PendingDeletion> deletions = pendingDeletionsByFile.get(fileId);
    if (deletions != null && !deletions.isEmpty()) {
      List<PendingDeletion> remappedDeletions = new ArrayList<>();
      for (PendingDeletion deletion : deletions) {
        if (deletion.commentId().startsWith("local_")) {
          String remoteId = fileMappings.get(deletion.commentId());
          if (remoteId != null) {
            PendingDeletion remapped =
                new PendingDeletion(
                    deletion.fileId(), remoteId, deletion.deletedAt(), deletion.status());
            remappedDeletions.add(remapped);
          } else {
            remappedDeletions.add(deletion);
          }
        } else {
          remappedDeletions.add(deletion);
        }
      }
      pendingDeletionsByFile.put(fileId, remappedDeletions);
    }
  }

  private String generateLocalId() {
    return "local_" + UUID.randomUUID().toString().substring(0, 8);
  }

  public void cleanUpCommentActions(String fileId, String commentId) {
    List<PendingReply> replies = pendingRepliesByFile.get(fileId);
    List<PendingResolution> resolutions = pendingResolutionsByFile.get(fileId);
    if (replies == null && resolutions == null) return;

    if (replies != null) {
      replies.removeIf(reply -> reply.parentCommentId().equals(commentId));
    }

    if (resolutions != null) {
      resolutions.removeIf(resolution -> resolution.commentId().equals(commentId));
    }
  }

  public Map<String, String> getIdMappingsForFile(String fileId) {
    return idMappingsByFile.getOrDefault(fileId, new HashMap<>());
  }

  public void clearAllPendingComments() {
    pendingCommentsByFile.clear();
    pendingRepliesByFile.clear();
    pendingResolutionsByFile.clear();
    pendingDeletionsByFile.clear();
  }

  public List<String> getFilesWithPendingChanges() {
    Set<String> files = new HashSet<>();
    files.addAll(pendingCommentsByFile.keySet());
    files.addAll(pendingRepliesByFile.keySet());
    files.addAll(pendingResolutionsByFile.keySet());
    files.addAll(pendingDeletionsByFile.keySet());

    return new ArrayList<>(files);
  }
}
