package com.codecamp.chatapp.remote.database.user.current_user;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class CurrentUserHelper {
    private String userId;
    private DatabaseReference mRef;
    public CurrentUserHelper(){
        try{
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        }catch (Exception e){

        }

    }
    public void fetchCurrentPerson(final ValueEventListener listener){
        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.onDataChange(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error);
            }
        });
    }

    public void disconnectUser(){
        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("time_stamp")
                .onDisconnect()
                .setValue(ServerValue.TIMESTAMP);
    }

    public void disconnectResult(String status){
        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("status")
                .onDisconnect()
                .setValue(status);
    }

    public void connectResult(String status){
        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("status")
                .setValue(status);
    }

    public String getUserId(){
        return userId;
    }
}
