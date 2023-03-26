package com.codecamp.chatapp.remote.database.notifications;

public interface NotificationStrategy {
    void sendNotification(String sender,String receiver, String name, String message);
}
