package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CommentManager {
  private static final CommentManager INSTANCE = new CommentManager();


  @Getter
  private final Map<String, List<PendingComment>> comments;
  private final Map<String, Map<String, String>> idMappingsByFile = new ConcurrentHashMap<>();

  private CommentManager() {
    this.comments = new ConcurrentHashMap<>();
  }

  public static CommentManager getInstance() {
    return INSTANCE;
  }

  public List<PendingComment> getPendingsByFileId(String fileId){
    return this.comments.getOrDefault(fileId, List.of());
  }

  public List<PendingComment> getAllPendings() {
    return comments.values().stream()
            .flatMap(List::stream)
            .toList();
  }

  public void add(String fileId, PendingComment comment){
    this.comments.computeIfAbsent(fileId, ignored -> new ArrayList<>()).add(comment);
  }

  public List<PendingComment> getAddComments() {    // really need it for the merge?
      return getAllPendings().stream()
              .filter(comment -> comment instanceof AddComment)
              .toList();
  }

  public List<PendingComment> getReplyComments() {
      return getAllPendings().stream()
              .filter(comment -> comment instanceof ReplyComment)
              .toList();
  }

  public List<PendingComment> getResolveComments() {
    return getAllPendings().stream()
            .filter(comment -> comment instanceof ResolveComment)
            .toList();
  }

  public List<PendingComment> getDeleteComments() {
    return getAllPendings().stream()
            .filter(comment -> comment instanceof DeleteComment)
            .toList();
  }

  public Map<String, String> getIdMappingsForFile(String fileId) {
    return idMappingsByFile.getOrDefault(fileId, new HashMap<>());
  }
}