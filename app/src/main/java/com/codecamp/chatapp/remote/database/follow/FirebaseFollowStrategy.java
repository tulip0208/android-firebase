package com.codecamp.chatapp.remote.database.follow;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FirebaseFollowStrategy implements FollowStrategy{
    @Override
    public void follow(String userId) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Followers")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("followStatus","Follow");
                            hashMap.put("photo","default");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Followers")
                                    .child(userId)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMap);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void getFollowStatus(String userId, FollowHandler.OnFollowStatus onFollowStatus) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Followers")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            if(dataSnapshot.getRef().getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                String followStatus = dataSnapshot.child("followStatus").getValue().toString();
                                onFollowStatus.onFollowStatusChanged(followStatus);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void setFollowStatus(String userId) {
        FirebaseDatabase.getInstance().getReference()
                .child("Followers")
                .child(userId)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String followStatus=snapshot.child("followStatus").getValue().toString();
                        if(followStatus.equals("Follow")){
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("Followers")
                                    .child(userId)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("followStatus")
                                    .setValue("Following");
                        }else{
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("Followers")
                                    .child(userId)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("followStatus")
                                    .setValue("Follow");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void getFollowers(String userId,FollowerStrategy followerStrategy) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Followers")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        followerStrategy.onClear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String photo=dataSnapshot.child("photo").getValue().toString();
                            followerStrategy.onFollowersFetched(photo);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        followerStrategy.onError(error.getMessage());
                    }
                });
    }

    public interface FollowerStrategy{
        void onClear();
        void onFollowersFetched(String photo);
        void onError(String error);
    }

}
