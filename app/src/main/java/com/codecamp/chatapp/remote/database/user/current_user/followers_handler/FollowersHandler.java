package com.codecamp.chatapp.remote.database.user.current_user.followers_handler;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FollowersHandler {
    public List<String> followersList;
    public FollowersHandler(){
        followersList = new ArrayList<>();
    }
    public void getFollowersCount(final FollowersTask followersTask){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Followers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                for(DataSnapshot followSnapshot : dataSnapshot.getChildren()){
                                    followersList.add(followSnapshot.getKey());
                                }
                                followersTask.onTotalFollowers(followersList);
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
