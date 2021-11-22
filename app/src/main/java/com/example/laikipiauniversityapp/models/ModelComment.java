package com.example.laikipiauniversityapp.models;


public class ModelComment {

    String id, bookId,timeStamp,comment,uid;

    public ModelComment() {
    }

    public ModelComment(String id, String bookId, String timeStamp, String comment, String uid) {

        this.id = id;
        this.bookId = bookId;
        this.timeStamp = timeStamp;
        this.comment = comment;
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
