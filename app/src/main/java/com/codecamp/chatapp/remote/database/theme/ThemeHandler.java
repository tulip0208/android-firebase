package com.codecamp.chatapp.remote.database.theme;

public class ThemeHandler {
    private ThemeStrategy themeStrategy;
    public ThemeHandler(ThemeStrategy themeStrategy){
        this.themeStrategy = themeStrategy;
    }

    public void theme(String userId){
        themeStrategy.theme(userId);
    }

    public void getTheme(String userId, ThemeStrategy.OnTheme onTheme){
        themeStrategy.getTheme(userId,onTheme);
    }

    public void setTheme(String userId, String theme){
        themeStrategy.setTheme(userId,theme);
    }
}
