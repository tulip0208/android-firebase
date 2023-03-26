package com.codecamp.chatapp.bindings.models;

public class Follower {
    private String followerName;
    private String followerImage;
    private String userId;

    public Follower() {
    }

    public Follower(String followerName, String followerImage,String userId) {
        this.followerName = followerName;
        this.followerImage = followerImage;
        this.userId = userId;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getFollowerImage() {
        return followerImage;
    }

    public void setFollowerImage(String followerImage) {
        this.followerImage = followerImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
