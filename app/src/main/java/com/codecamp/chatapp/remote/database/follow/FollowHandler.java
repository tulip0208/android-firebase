package com.codecamp.chatapp.remote.database.follow;

public class FollowHandler {
    private FollowStrategy followStrategy;

    public FollowHandler(FollowStrategy followStrategy) {
        this.followStrategy = followStrategy;
    }

    public void setFollowStrategy(FollowStrategy followStrategy){
        this.followStrategy = followStrategy;
    }

    public void follow(String userId){
        followStrategy.follow(userId);
    }

    public void getFollowStatus(String userId, final OnFollowStatus onFollowStatus){
        followStrategy.getFollowStatus(userId, onFollowStatus);
    }

    public void setFollowStatus(String userId){
        followStrategy.setFollowStatus(userId);
    }

    public void getFollowers(String userId, FirebaseFollowStrategy.FollowerStrategy followerStrategy){
        followStrategy.getFollowers(userId,followerStrategy);
    }

    public interface OnFollowStatus{
        public void onFollowStatusChanged(String followStatus);
    }
}
