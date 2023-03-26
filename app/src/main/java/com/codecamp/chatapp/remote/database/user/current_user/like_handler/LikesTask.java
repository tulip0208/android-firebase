package com.codecamp.chatapp.remote.database.user.current_user.like_handler;

import java.util.List;

public interface LikesTask {
    void onTotalLikes(List<String> likesList);
}
