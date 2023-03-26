package com.codecamp.chatapp.remote.auth.logout;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LogoutHandler {
    public void logout(final LogoutTask logoutTask){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("isLoggedIn").setValue("no");
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("status")
                .setValue("offline")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            logoutTask.onSuccess("you have signed out");
                        }else{
                            logoutTask.onFailure("oops!something went wrong");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        logoutTask.onFailure(e.getMessage());
                    }
                });
    }
}
