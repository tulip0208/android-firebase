package com.codecamp.chatapp.remote.database.like;

public interface LikeStrategy {
    void like(String userId);
    void getLikeStatus(String userId, LikeHandler.OnLikeStatus onLikeStatus);
    void setLikeStatus(String userId);
}
