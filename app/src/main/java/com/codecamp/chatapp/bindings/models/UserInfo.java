package com.codecamp.chatapp.bindings.models;

public class UserInfo {
    public String name;
    public String email;
    public String phone;
    public String photo;
    public String bio;
    public String status;
    public String typingStatus;
    public String time_stamp;

    public UserInfo(String name, String email, String phone, String photo, String bio, String status,String typingStatus,String time_stamp) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
        this.bio = bio;
        this.status = status;
        this.typingStatus = typingStatus;
        this.time_stamp = time_stamp;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTypingStatus() {
        return typingStatus;
    }

    public void setTypingStatus(String typingStatus) {
        this.typingStatus = typingStatus;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }
}
