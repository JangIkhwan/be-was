package model;

public class Article {
    private Long id;
    private Long creatorId;
    private String title;
    private String content;

    public Article(Long creatorId, String title, String content) {
        this.creatorId = creatorId;
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
