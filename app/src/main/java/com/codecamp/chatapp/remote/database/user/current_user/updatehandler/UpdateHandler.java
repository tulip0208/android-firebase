package com.codecamp.chatapp.remote.database.user.current_user.updatehandler;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateHandler {
    public void updateName(String name,final OnCompleteListener listener,final OnFailureListener onFailureListener){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("name")
                .setValue(name)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete(task);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onFailureListener.onFailure(e);
            }
        });
    }

    public void updateBio(String bio,final OnCompleteListener listener,final OnFailureListener onFailureListener){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("bio")
                .setValue(bio)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete(task);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onFailureListener.onFailure(e);
            }
        });
    }

    public void updateAbout(String about,final OnCompleteListener listener,final OnFailureListener onFailureListener){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("about")
                .setValue(about)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete(task);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onFailureListener.onFailure(e);
            }
        });
    }

    public void updatePhone(String phone,final OnCompleteListener listener,final OnFailureListener onFailureListener){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("phone")
                .setValue(phone)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete(task);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onFailureListener.onFailure(e);
            }
        });
    }
}
