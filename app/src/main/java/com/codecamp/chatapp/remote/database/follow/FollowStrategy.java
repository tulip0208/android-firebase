package com.codecamp.chatapp.remote.database.follow;

public interface FollowStrategy {
    void follow(String userId);
    void getFollowStatus(String userId, FollowHandler.OnFollowStatus onFollowStatus);
    void setFollowStatus(String userId);
    void getFollowers(String userId, FirebaseFollowStrategy.FollowerStrategy followerStrategy);
}
