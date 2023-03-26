package com.codecamp.chatapp.bindings.models;

public class User {
    public String id;
    public String name;
    public String image;
    public String email;
    public String password;
    public String status;
    public Object timestamp;
    public String bio;
    public String phone;
    public String about;



    public User() {
    }

    public User(String id,String name, String image, String email, String password,String status,Object timestamp,String bio,String phone,String about) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.password = password;
        this.status = status;
        this.timestamp = timestamp;
        this.bio = bio;
        this.phone = phone;
        this.about = about;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
