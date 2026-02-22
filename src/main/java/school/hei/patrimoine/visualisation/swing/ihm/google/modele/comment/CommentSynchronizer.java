package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment;

public class CommentSynchronizer {
    private final CommentManager commentManager;

    public CommentSynchronizer() {
        this.commentManager = CommentManager.getInstance();
    }

    public void sync(String fileId, PendingComment comment) {
        switch (comment) {
            case ReplyComment reply:
                break;
            case AddComment add:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + comment);
        }
    }

    public void sync() {
        for (var entry : commentManager.getComments().entrySet()) {
            entry.getValue().forEach(comment -> sync(entry.getKey(), comment));
        }
    }
}
