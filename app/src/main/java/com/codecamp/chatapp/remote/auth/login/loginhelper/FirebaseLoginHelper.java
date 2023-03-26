package com.codecamp.chatapp.remote.auth.login.loginhelper;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseLoginHelper {
    private FirebaseAuth mAuth;

    public FirebaseLoginHelper() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void signInWithEmailAndPassword(String email, String password, final OnCompleteListener<AuthResult> listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        listener.onComplete(task);
                    }
                });
    }

}
