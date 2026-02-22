package school.hei.patrimoine.visualisation.swing.ihm.google.modele.mapper;

import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.AddComment;

public class CommentMapper {
    public static Comment toComment(AddComment addComment){
        return Comment.builder().build();
    }

  //  public
}
