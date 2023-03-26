package com.codecamp.chatapp.remote.database.like;

public class LikeHandler {
    private LikeStrategy likeStrategy;

    public LikeHandler(LikeStrategy likeStrategy) {
        this.likeStrategy = likeStrategy;
    }

    public void setLikeStrategy(LikeStrategy likeStrategy) {
        this.likeStrategy = likeStrategy;
    }

    public void like(String userId) {
        likeStrategy.like(userId);
    }

    public void getLikeStatus(String userId, OnLikeStatus onLikeStatus) {
        likeStrategy.getLikeStatus(userId, onLikeStatus);
    }

    public void setLikeStatus(String userId) {
        likeStrategy.setLikeStatus(userId);
    }

    public interface OnLikeStatus {
        void onLikeStatusChanged(String likeStatus);
    }
}
