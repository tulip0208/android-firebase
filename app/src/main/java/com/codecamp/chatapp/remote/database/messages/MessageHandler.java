package com.codecamp.chatapp.remote.database.messages;

import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessageHandler {
    private MessageStrategy messageStrategy;

    public MessageHandler(MessageStrategy messageStrategy){
        this.messageStrategy = messageStrategy;
    }

    public void startTyping(String currentUserId,String typingID){
        messageStrategy.startTyping(currentUserId,typingID);
    }

    public void sendMessage(String sender, String receiver, String message){
        messageStrategy.sendMessage(sender,receiver,message);
    }

    public void sendImage(Intent data, String sender, String receiver, final FirebaseMessageStrategy.OnUploadFileTask uploadFileTask){
        messageStrategy.sendImage(data,sender,receiver,uploadFileTask);
    }

    public void seenMessage(String currentUserId,String userID){
        messageStrategy.seenMessage(currentUserId,userID);
    }

    public void readMessage(String currentUserID, String userID, String imageUrl, final FirebaseMessageStrategy.OnMessageTask onMessageTask){
        messageStrategy.readMessage(currentUserID,userID,imageUrl,onMessageTask);
    }
}
