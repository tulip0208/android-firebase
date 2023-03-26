package com.codecamp.chatapp.remote.database.like;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseLikeStrategy implements LikeStrategy {
    @Override
    public void like(String userId) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Likes")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("Likes")
                                    .child(userId)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("likeStatus")
                                    .setValue("Like");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void getLikeStatus(String userId, LikeHandler.OnLikeStatus onLikeStatus) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Likes")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.getRef().getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                String likeStatus = dataSnapshot.child("likeStatus").getValue().toString();
                                onLikeStatus.onLikeStatusChanged(likeStatus);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void setLikeStatus(String userId) {
        FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(userId)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String likeStatus = snapshot.child("likeStatus").getValue().toString();
                        if (likeStatus.equals("Like")) {
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("Likes")
                                    .child(userId)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("likeStatus")
                                    .setValue("Liked");
                        } else {
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("Likes")
                                    .child(userId)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("likeStatus")
                                    .setValue("Like");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
