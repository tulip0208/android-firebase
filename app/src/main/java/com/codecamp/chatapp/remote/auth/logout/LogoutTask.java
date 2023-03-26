package com.codecamp.chatapp.remote.auth.logout;

public interface LogoutTask {
    void onSuccess(String message);
    void onFailure(String error);
}
