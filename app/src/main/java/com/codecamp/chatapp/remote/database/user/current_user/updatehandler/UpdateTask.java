package com.codecamp.chatapp.remote.database.user.current_user.updatehandler;

public interface UpdateTask {
    void onUpdating();
    void onSuccess(String message);
    void onFailure(String error);
    void onDatabaseError(String error);
}
