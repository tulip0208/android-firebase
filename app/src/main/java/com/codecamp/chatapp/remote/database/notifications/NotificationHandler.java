package com.codecamp.chatapp.remote.database.notifications;

public class NotificationHandler {
    private NotificationStrategy notificationStrategy;

    public NotificationHandler(NotificationStrategy notificationStrategy){
        this.notificationStrategy = notificationStrategy;
    }

    public void sendNotification(String sender,String receiver, String name, String message){
        notificationStrategy.sendNotification(sender,receiver,name,message);
    }
}
