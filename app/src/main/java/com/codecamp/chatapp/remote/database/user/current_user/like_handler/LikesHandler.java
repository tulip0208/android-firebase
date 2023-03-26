package com.codecamp.chatapp.remote.database.user.current_user.like_handler;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LikesHandler {
    private List<String> likesList;
    public LikesHandler(){
        likesList = new ArrayList<>();
    }

    public void getLikesCount(final LikesTask likesTask){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Likes")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                for(DataSnapshot followSnapshot : dataSnapshot.getChildren()){
                                    likesList.add(followSnapshot.getKey());
                                }
                                likesTask.onTotalLikes(likesList);
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
