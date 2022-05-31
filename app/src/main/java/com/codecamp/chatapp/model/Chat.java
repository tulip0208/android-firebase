package com.codecamp.chatapp.model;

public class Chat {
    private String sender;
    private String receiver;
    private String type;
    private String message;
    private String date;
    private Boolean isseen;
    private Boolean imgIsSeen;


    public Chat() {
    }

    public Chat(String sender, String receiver, String type, String message, String date, Boolean isseen, Boolean imgIsSeen) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.message = message;
        this.date = date;
        this.isseen = isseen;
        this.imgIsSeen = imgIsSeen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getIsseen() {
        return isseen;
    }

    public void setIsseen(Boolean isseen) {
        this.isseen = isseen;
    }
    public Boolean getImgIsSeen(){
        return imgIsSeen;
    }
    public void setImgIsSeen(Boolean imgIsSeen){
        this.imgIsSeen = imgIsSeen;
    }
}
