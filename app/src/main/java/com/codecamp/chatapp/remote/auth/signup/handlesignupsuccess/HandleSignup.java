package com.codecamp.chatapp.remote.auth.signup.handlesignupsuccess;

import android.app.Application;

import androidx.annotation.NonNull;

import com.codecamp.chatapp.remote.auth.signup.SignupView;
import com.codecamp.chatapp.remote.error_handler.FirebaseErrorHandler;
import com.codecamp.chatapp.utility.toast.ToastController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class HandleSignup extends Application {
    private SignupView signupView;
    private ToastController toastController;
    private FirebaseErrorHandler firebaseErrorHandler;

    public HandleSignup(SignupView signupView) {
        this.signupView = signupView;
        toastController = new ToastController();
        firebaseErrorHandler = new FirebaseErrorHandler();
    }

    public void saveData(String name, String email, String phone, String password) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("phone", phone);
        hashMap.put("about", "default");
        hashMap.put("image", "default");
        hashMap.put("email", email);
        hashMap.put("password", password);
        hashMap.put("bio", "default");
        hashMap.put("status", "offline");
        hashMap.put("typingTo", "noOne");
        hashMap.put("time_stamp", "default");
        hashMap.put("message_sender", "default");
        hashMap.put("isLoggedIn", "no");
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            signupView.showSignupSuccess();
                        } else {
                            signupView.showSignupFailure();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                signupView.showSignupFailure();
                firebaseErrorHandler.signupError(e.getMessage().toString());
            }
        });
    }

    public void dataSaveFailed() {
        toastController.toast(this, "data save failed!!");
    }
}
