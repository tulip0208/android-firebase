package com.codecamp.chatapp.remote.database.user.current_user;

public interface OnFetchCurrentUser {
    void onFetched(CurrentUser currentUser);
    void onFetchFailed(String error);
}
