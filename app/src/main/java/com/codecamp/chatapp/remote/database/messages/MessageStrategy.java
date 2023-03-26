package com.codecamp.chatapp.remote.database.messages;

import android.content.Intent;

public interface MessageStrategy {
    void startTyping(String currentUserId,String typingID);
    void sendMessage(String sender,String receiver,String message);
    void sendImage(Intent data, String sender, String receiver, final FirebaseMessageStrategy.OnUploadFileTask uploadFileTask);
    void seenMessage(String currentUserId,String userID);
    void readMessage(String currentUserID, String userID, String imageUrl, final FirebaseMessageStrategy.OnMessageTask onMessageTask);
}
