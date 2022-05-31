package com.codecamp.chatapp.model;

public class User {
    public String name;
    public String image;
    public String email;
    public String password;
    public String status;
    public Object timestamp;
    public String bio;



    public User() {
    }

    public User(String name, String image, String email, String password,String status,Object timestamp,String bio) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.password = password;
        this.status = status;
        this.timestamp = timestamp;
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
