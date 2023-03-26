package com.codecamp.chatapp.remote.database.user.current_user.followers_handler;

import java.util.List;

public interface FollowersTask {
    void onTotalFollowers(List<String> totalFollowers);
}
