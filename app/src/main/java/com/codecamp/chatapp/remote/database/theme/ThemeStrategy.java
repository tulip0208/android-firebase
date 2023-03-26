package com.codecamp.chatapp.remote.database.theme;

public interface ThemeStrategy {
    void theme(String userId);
    void getTheme(String userId,OnTheme onTheme);
    void setTheme(String userId,String theme);
    interface OnTheme{
        void onThemeChanged(String theme);
    }
}
