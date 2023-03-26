package com.codecamp.chatapp.remote.database.theme;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseThemeStrategy implements ThemeStrategy{
    @Override
    public void theme(String userId) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Themes")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("Themes")
                                    .child(userId)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("theme")
                                    .setValue("default");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void getTheme(String userId, OnTheme onTheme) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Themes")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            if(dataSnapshot.getRef().getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                String theme=dataSnapshot.child("theme").getValue().toString();
                                onTheme.onThemeChanged(theme);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void setTheme(String userId, String theme) {
        FirebaseDatabase.getInstance().getReference()
                .child("Themes")
                .child(userId)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("theme")
                .setValue(theme);
    }
}
