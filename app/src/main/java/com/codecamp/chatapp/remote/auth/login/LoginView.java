package com.codecamp.chatapp.remote.auth.login;

public interface LoginView {
    void showLoginSuccess();
    void showLoginFailure();
    void onLoggedIn(String loggedInStatus);
}
