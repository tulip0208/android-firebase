package com.codecamp.chatapp.remote.auth.signup.signuphelper;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseSignupHelper {
    private FirebaseAuth mAuth;
    public FirebaseSignupHelper(){
        mAuth = FirebaseAuth.getInstance();
    }
    public void createUserWithEmailAndPassword(String email,String password,final OnCompleteListener<AuthResult> listener){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        listener.onComplete(task);
                    }
                });
    }
}
