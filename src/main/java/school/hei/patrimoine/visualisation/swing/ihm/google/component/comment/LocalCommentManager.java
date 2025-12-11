package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
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
        PendingComment.builder()
            .localId(localId)
            .fileId(fileId)
            .content(content)
            .createdAt(Instant.now())
            .author(author)
            .status(SyncStatus.PENDING_ADD)
            .build();

    pendingCommentsByFile.computeIfAbsent(fileId, k -> new ArrayList<>()).add(pendingComment);
  }

  public List<PendingComment> getPendingComments(String fileId) {
    return pendingCommentsByFile.getOrDefault(fileId, new ArrayList<>());
  }

  public void removePendingComment(String fileId, String localId) {
    List<PendingComment> comments = pendingCommentsByFile.get(fileId);
    if (comments == null) return;

    comments.removeIf(comment -> comment.localId().equals(localId));

    if (comments.isEmpty()) {
      pendingCommentsByFile.remove(fileId);
    }
  }

  public void addPendingReply(String fileId, String parentCommentId, String content, User author) {
    String localId = generateLocalId();
    var pendingReply =
        PendingReply.builder()
            .localId(localId)
            .fileId(fileId)
            .parentCommentId(parentCommentId)
            .content(content)
            .createdAt(Instant.now())
            .author(author)
            .status(SyncStatus.PENDING_REPLY)
            .build();

    pendingRepliesByFile.computeIfAbsent(fileId, k -> new ArrayList<>()).add(pendingReply);
  }

  public List<PendingReply> getPendingReplies(String fileId) {
    return pendingRepliesByFile.getOrDefault(fileId, new ArrayList<>());
  }

  public void removePendingReply(String fileId, String localId) {
    List<PendingReply> replies = pendingRepliesByFile.get(fileId);
    if (replies == null) return;

    replies.removeIf(reply -> reply.localId().equals(localId));

    if (replies.isEmpty()) {
      pendingRepliesByFile.remove(fileId);
    }
  }

  public void addPendingResolution(String fileId, String commentId) {
    var pendingResolution =
        PendingResolution.builder()
            .fileId(fileId)
            .commentId(commentId)
            .resolvedAt(Instant.now())
            .status(SyncStatus.PENDING_RESOLVE)
            .build();

    pendingResolutionsByFile.computeIfAbsent(fileId, k -> new ArrayList<>()).add(pendingResolution);
  }

  public List<PendingResolution> getPendingResolutions(String fileId) {
    return pendingResolutionsByFile.getOrDefault(fileId, new ArrayList<>());
  }

  public void removePendingResolution(String fileId, String commentId) {
    List<PendingResolution> resolutions = pendingResolutionsByFile.get(fileId);
    if (resolutions == null) return;

    resolutions.removeIf(resolution -> resolution.commentId().equals(commentId));

    if (resolutions.isEmpty()) {
      pendingResolutionsByFile.remove(fileId);
    }
  }

  public void addPendingDeletion(String fileId, String commentId) {
    var pendingDeletion =
        PendingDeletion.builder()
            .fileId(fileId)
            .commentId(commentId)
            .deletedAt(Instant.now())
            .status(SyncStatus.PENDING_DELETE)
            .build();

    pendingDeletionsByFile.computeIfAbsent(fileId, k -> new ArrayList<>()).add(pendingDeletion);
  }

  public List<PendingDeletion> getPendingDeletions(String fileId) {
    return pendingDeletionsByFile.getOrDefault(fileId, new ArrayList<>());
  }

  public void removePendingDeletion(String fileId, String commentId) {
    List<PendingDeletion> deletions = pendingDeletionsByFile.get(fileId);
    if (deletions == null) return;

    deletions.removeIf(deletion -> deletion.commentId().equals(commentId));

    if (deletions.isEmpty()) {
      pendingDeletionsByFile.remove(fileId);
    }
  }

  public void remapPendingActions(String fileId, Map<String, String> newMappings) {
    Map<String, String> fileMappings =
        idMappingsByFile.computeIfAbsent(fileId, k -> new ConcurrentHashMap<>());
    fileMappings.putAll(newMappings);

    remapPendingItems(
        fileId,
        fileMappings,
        pendingRepliesByFile,
        PendingReply::parentCommentId,
        (reply, remoteId) ->
            PendingReply.builder()
                .localId(reply.localId())
                .fileId(reply.fileId())
                .parentCommentId(remoteId)
                .content(reply.content())
                .author(reply.author())
                .status(reply.status())
                .build());

    remapPendingItems(
        fileId,
        fileMappings,
        pendingResolutionsByFile,
        PendingResolution::commentId,
        (resolution, remoteId) ->
            PendingResolution.builder()
                .fileId(resolution.fileId())
                .commentId(remoteId)
                .resolvedAt(resolution.resolvedAt())
                .status(resolution.status())
                .build());

    remapPendingItems(
        fileId,
        fileMappings,
        pendingDeletionsByFile,
        PendingDeletion::commentId,
        (deletion, remoteId) ->
            PendingDeletion.builder()
                .fileId(deletion.fileId())
                .commentId(remoteId)
                .deletedAt(deletion.deletedAt())
                .status(deletion.status())
                .build());
  }

  private <T> void remapPendingItems(
      String fileId,
      Map<String, String> fileMappings,
      Map<String, List<T>> storage,
      Function<T, String> idExtractor,
      BiFunction<T, String, T> remapper) {

    List<T> items = storage.get(fileId);
    if (items == null || items.isEmpty()) {
      return;
    }

    List<T> remapped =
        items.stream()
            .map(item -> remapItemIfNeeded(item, fileMappings, idExtractor, remapper))
            .toList();
    storage.put(fileId, remapped);
  }

  private <T> T remapItemIfNeeded(
      T item,
      Map<String, String> fileMappings,
      Function<T, String> idExtractor,
      BiFunction<T, String, T> remapper) {

    String currentId = idExtractor.apply(item);

    if (!currentId.startsWith("local_")) {
      return item;
    }

    String remoteId = fileMappings.get(currentId);
    if (remoteId != null) {
      return remapper.apply(item, remoteId);
    }
    return item;
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

      if (replies.isEmpty()) {
        pendingRepliesByFile.remove(fileId);
      }
    }

    if (resolutions != null) {
      resolutions.removeIf(resolution -> resolution.commentId().equals(commentId));

      if (resolutions.isEmpty()) {
        pendingResolutionsByFile.remove(fileId);
      }
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
