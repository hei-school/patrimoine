package school.hei.patrimoine.google.model;

public record Pagination(Integer pageSize, String pageToken){
    public String createCacheKey(String fileId){
        return String.format("%s%s",  fileId, pageSize);
    }
}
