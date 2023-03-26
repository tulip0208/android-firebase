package com.codecamp.chatapp.remote.database.user.current_user;

public class CurrentUser {
    private String name;
    private String email;
    private String image;
    private String status;
    private String bio;
    private String timestamp;
    private String phone;
    private String about;

    public CurrentUser(String name, String email, String image, String status, String bio, String timestamp, String phone, String about) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.status = status;
        this.bio = bio;
        this.timestamp = timestamp;
        this.phone = phone;
        this.about = about;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
