package com.scrapper.i170303_i170364_project;

public class OfflinePost
{
    private String authorName;
    private String uploadTime;
    private String title;
    private String content;
    private String postImageAdress;
    private String postAuthorImageAdress;


    public OfflinePost(String authorName, String uploadTime, String title, String content, String postImageAdress, String postAuthorImageAdress) {
        this.authorName = authorName;
        this.uploadTime = uploadTime;
        this.title = title;
        this.content = content;
        this.postImageAdress = postImageAdress;
        this.postAuthorImageAdress = postAuthorImageAdress;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostImageAdress() {
        return postImageAdress;
    }

    public void setPostImageAdress(String postImageAdress) {
        this.postImageAdress = postImageAdress;
    }

    public String getPostAuthorImageAdress() {
        return postAuthorImageAdress;
    }

    public void setPostAuthorImageAdress(String postAuthorAdress) {
        this.postAuthorImageAdress = postAuthorAdress;
    }
}
