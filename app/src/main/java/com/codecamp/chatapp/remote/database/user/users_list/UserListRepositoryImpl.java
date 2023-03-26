package com.codecamp.chatapp.remote.database.user.users_list;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserListRepositoryImpl implements UserListRepository{
    @Override
    public void getUsersList(ValueEventListener listener) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
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
}
