package com.codecamp.chatapp.remote.database.user.current_user.followers_handler;

public class Followers {
    private String photo;

    public Followers(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
