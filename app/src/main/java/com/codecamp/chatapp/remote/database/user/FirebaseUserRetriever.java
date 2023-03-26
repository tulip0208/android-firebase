package com.codecamp.chatapp.remote.database.user;

import androidx.annotation.NonNull;

import com.codecamp.chatapp.bindings.models.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUserRetriever {
    public void retrieveUser(String userId, final UserRetrievedListener listener){
        FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue().toString();
                        String photo = snapshot.child("image").getValue().toString();
                        String bio = snapshot.child("bio").getValue().toString();
                        String status = snapshot.child("status").getValue().toString();
                        String email = snapshot.child("email").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String typingStatus = snapshot.child("typingTo").getValue().toString();
                        String time_stamp = snapshot.child("time_stamp").getValue().toString();
                        UserInfo userInfo = new UserInfo(name, email, phone, photo, bio, status,typingStatus,time_stamp);
                        listener.onUserRetrieved(userInfo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public interface UserRetrievedListener {
        void onUserRetrieved(UserInfo userInfo);
    }
}
