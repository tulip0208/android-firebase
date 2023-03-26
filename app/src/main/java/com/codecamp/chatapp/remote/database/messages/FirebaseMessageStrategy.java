package com.codecamp.chatapp.remote.database.messages;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.codecamp.chatapp.bindings.models.Chat;
import com.codecamp.chatapp.remote.database.notifications.FirebaseNotificationStrategy;
import com.codecamp.chatapp.remote.database.notifications.NotificationHandler;
import com.codecamp.chatapp.remote.database.notifications.NotificationStrategy;
import com.codecamp.chatapp.remote.retrofit.ApiServiceHandler;
import com.codecamp.chatapp.service.APIService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FirebaseMessageStrategy implements MessageStrategy{
    private DatabaseReference messageRef;
    private Boolean notify;
    private NotificationStrategy notificationStrategy;
    private APIService apiService;
    private ApiServiceHandler apiServiceHandler;
    private NotificationHandler notificationHandler;

    public FirebaseMessageStrategy(){
        notify = false;
        messageRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        apiServiceHandler = new ApiServiceHandler();
        apiService = apiServiceHandler.provideApiService();
        notificationStrategy = new FirebaseNotificationStrategy(apiService);
        notificationHandler = new NotificationHandler(notificationStrategy);
    }

    @Override
    public void startTyping(String currentUserId,String typingID) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(currentUserId)
                .child("typingTo")
                .setValue(typingID);
    }

    @Override
    public void sendMessage(String sender, String receiver, String message) {
        notify = true;
        String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("type","message");
        hashMap.put("message",message);
        hashMap.put("date",date);
        hashMap.put("isseen",false);
        hashMap.put("imgisseen",false);
        messageRef.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Users")
                            .child(receiver)    //userId==receiver
                            .child("message_sender")
                            .child(sender)  //sender==currentUserId
                            .child("message_date")
                            .setValue(date);
//                    messageSend.getText().clear();
//                    messageRecyclerview.scrollToPosition(chatList.size()-1);
//                    Toast.makeText(getApplicationContext(),"message sent",Toast.LENGTH_SHORT).show();
                }
            }
        });

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(sender)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue().toString();
                        if(notify)
                        {
                            //sendNotificcation(receiver,name,message);
                            notificationHandler.sendNotification(sender,receiver,name,message);
                        }

                        notify = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public void sendImage(Intent data, String sender, String receiver,final OnUploadFileTask uploadFileTask) {
        uploadFileTask.onLoading();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("ChatsImage").child(sender).child("images");
        Uri imageUri = data.getData();
        if(imageUri != null)
        {
            StorageReference filePath = mStorageRef.child(imageUri.getLastPathSegment()+ UUID.randomUUID());
            final UploadTask uploadTask = filePath.putFile(imageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if(task.isSuccessful())
                            {
                                uploadFileTask.onLoaded("file sent");
                                Uri downloadUri = task.getResult();
                                String downloadImageUrl=  downloadUri.toString();
                                String date = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                HashMap<String,Object> chatMap = new HashMap<>();
                                chatMap.put("sender",sender);
                                chatMap.put("receiver",receiver);
                                chatMap.put("type","image");
                                chatMap.put("message",downloadImageUrl);
                                chatMap.put("date",date);
                                chatMap.put("isseen",false);
                                chatMap.put("imgisseen",false);
                                FirebaseDatabase
                                        .getInstance()
                                        .getReference()
                                        .child("Chats")
                                        .push().setValue(chatMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            uploadFileTask.onLoaded("file sent");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void seenMessage(String currentUserId,String userID) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Chats")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            String receiver = dataSnapshot.child("receiver").getValue().toString();
                            String sender = dataSnapshot.child("sender").getValue().toString();
                            if(receiver.equals(currentUserId) && sender.equals(userID))
                            {
                                HashMap<String,Object> seenHashMap = new HashMap<>();
                                seenHashMap.put("isseen",true);
                                dataSnapshot.getRef().updateChildren(seenHashMap);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void readMessage(String currentUserID, String userID, String imageUrl,final OnMessageTask onMessageTask) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Chats")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Chat> chatList = new ArrayList<>();
                        chatList.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            String sender = snapshot.child("sender").getValue().toString();
                            String receiver = snapshot.child("receiver").getValue().toString();
                            String type = snapshot.child("type").getValue().toString();
                            String message = snapshot.child("message").getValue().toString();
                            String date = snapshot.child("date").getValue().toString();
                            Boolean isseen = Boolean.valueOf(snapshot.child("isseen").getValue().toString());
                            Boolean imgIsSeen = Boolean.valueOf(snapshot.child("imgisseen").getValue().toString());
                            if(receiver.equals(currentUserID) && sender.equals(userID) || receiver.equals(userID) && sender.equals(currentUserID))
                            {
                                chatList.add(new Chat(sender,receiver,type,message,date,isseen,imgIsSeen));
                            }
                            onMessageTask.onMessageReadCompleted(imageUrl,chatList);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public interface OnMessageTask{
        void onMessageReadCompleted(String imageUrl,List<Chat> chatList);
    }

    public interface OnUploadFileTask{
        void onLoading();
        void onLoaded(String message);
    }
}
