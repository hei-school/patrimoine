package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment;

import static java.util.Comparator.comparing;
import static school.hei.patrimoine.google.api.CommentApi.COMMENTS_CACHE_KEY;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import school.hei.patrimoine.google.cache.ApiCache;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

public class PendingCommentManager {
  public static Map<String, Map<String, AbstractPendingComment>> map = new ConcurrentHashMap<>();

  public static List<AbstractPendingComment> getPendings() {
    return map.values().stream().map(Map::values).flatMap(Collection::stream).toList();
  }

  public static void add(AbstractPendingComment pending) {
    var subMap = getSubMap(pending.getFileId());
    var baseKey = getBaseKey(pending);
    switch (pending) {
      case AddComment ignored -> subMap.put(baseKey, pending);
      default -> {
        var group =
            (GroupedByComment)
                subMap.getOrDefault(baseKey, new GroupedByComment(pending.getFileId()));
        group.add(pending);
      }
    }
  }

  public static void clear() {
    map.clear();
    ApiCache.getInstance().invalidate(COMMENTS_CACHE_KEY);
  }

  public static void remove(AbstractPendingComment pending) {
    var subMap = getSubMap(pending.getFileId());
    var baseKey = getBaseKey(pending);
    switch (pending) {
      case AddComment ignored -> subMap.remove(baseKey);
      default -> {
        var group =
            (GroupedByComment)
                subMap.getOrDefault(baseKey, new GroupedByComment(pending.getFileId()));
        group.remove(pending);
        if (group.isEmpty()) {
          subMap.remove(baseKey);
        }
      }
    }
  }

  public static List<AbstractPendingComment> getByFile(PatriLangFileContext file) {
    return getSubMap(file.getDriveId()).values().stream()
        .sorted(comparing(AbstractPendingComment::getCreatedAt))
        .toList();
  }

  private static Map<String, AbstractPendingComment> getSubMap(String fileId) {
    if (!map.containsKey(fileId)) {
      map.put(fileId, new ConcurrentHashMap<>());
    }

    return map.get(fileId);
  }

  private static String getBaseKey(AbstractPendingComment pending) {
    return switch (pending) {
      case AddComment toAdd -> toAdd.getLocalId();
      case ReplyComment toReply -> toReply.getComment().getId();
      case ResolveComment toResolve -> toResolve.getComment().getId();
      case DeleteComment toDelete -> toDelete.getComment().getId();
      default -> throw new RuntimeException("Invalid type of PendingComment");
    };
  }
}
