package school.hei.patrimoine.google.api;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import school.hei.patrimoine.google.cache.ApiCache;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.google.mapper.CommentMapper;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.model.PaginatedResult;
import school.hei.patrimoine.google.model.Pagination;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.CommentMerger;

public class CommentApi {
  private final DriveApi driveApi;
  private final ApiCache apiCache;
  private final CommentMapper commentMapper;
  private final CommentMerger commentMerger;
  public static final String COMMENTS_CACHE_KEY = "comments";

  public CommentApi(DriveApi driveApi, CommentMerger commentMerger) {
    this.driveApi = driveApi;
    this.apiCache = ApiCache.getInstance();
    this.commentMapper = CommentMapper.getInstance();
    this.commentMerger = commentMerger;
  }

  public PaginatedResult<List<Comment>> getByFileId(
      String fileId, Pagination pagination, Instant startDate) throws GoogleIntegrationException {

    return apiCache
        .wrap(
            COMMENTS_CACHE_KEY,
            pagination.createCacheKey(fileId + "_" + startDate),
            () -> {
              try {
                return getByFileIdWithoutCache(fileId, pagination, startDate);
              } catch (IOException e) {
                throw new GoogleIntegrationException(
                    "Failed to get comments for fileId=" + fileId, e);
              }
            })
        .get();
  }

  private PaginatedResult<List<Comment>> getByFileIdWithoutCache(
      String fileId, Pagination pagination, Instant startDate) throws IOException {

    var startDateStr = DateTimeFormatter.ISO_INSTANT.format(startDate);

    var commentList =
        driveApi
            .driveService()
            .comments()
            .list(fileId)
            .setIncludeDeleted(false)
            .setStartModifiedTime(startDateStr)
            .setPageSize(pagination.pageSize())
            .setPageToken(pagination.pageToken())
            .setFields(
                "comments(id,content,createdTime,resolved,"
                    + "author(displayName,emailAddress,photoLink,permissionId,me),"
                    + "replies(id,content,createdTime,author(displayName,emailAddress,photoLink,permissionId,me)))"
                    + ",nextPageToken")
            .execute();

    List<Comment> allComments = new ArrayList<>();

    if (commentList.getComments() != null) {
      var driveComments =
          commentList.getComments().stream()
              .map(commentMapper::toDomain)
              .sorted(Comparator.comparing(Comment::createdAt).reversed())
              .toList();
      allComments.addAll(driveComments);
    }
    var mergedComments = commentMerger.mergeLocalComments(fileId, allComments);

    var nextPageToken = commentList.getNextPageToken();
    return PaginatedResult.of(mergedComments, new Pagination(pagination.pageSize(), nextPageToken));
  }
}
