package com.example.laikipiauniversityapp.models;

public class ModelPdf {

    //    gettres
    String Uid;
    String id;
    String title;
    String description;
    public String categoryId;
    String Url;
    long timestamp,downloadsCount,viewsCount;
    boolean favorite;

    public ModelPdf() {
    }

    public ModelPdf(String uid, String id, String title, String description, String categoryId, String url, long timestamp, long downloadsCount, long viewsCount, boolean favorite) {
        Uid = uid;
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        Url = url;
        this.timestamp = timestamp;
        this.downloadsCount = downloadsCount;
        this.viewsCount = viewsCount;
        this.favorite = favorite;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDownloadsCount() {
        return downloadsCount;
    }

    public void setDownloadsCount(long downloadsCount) {
        this.downloadsCount = downloadsCount;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
