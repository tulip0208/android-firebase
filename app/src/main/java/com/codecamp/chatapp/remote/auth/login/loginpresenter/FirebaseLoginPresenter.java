package com.codecamp.chatapp.remote.auth.login.loginpresenter;

import androidx.annotation.NonNull;

import com.codecamp.chatapp.remote.auth.login.LoginView;
import com.codecamp.chatapp.remote.auth.login.loginhelper.FirebaseLoginHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseLoginPresenter {
    private FirebaseLoginHelper firebaseLoginHelper;
    public FirebaseLoginPresenter(){
        firebaseLoginHelper = new FirebaseLoginHelper();
    }

    public void login(String email,String password,final LoginView loginView){
        firebaseLoginHelper.signInWithEmailAndPassword(email, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("status").setValue("online");
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("isLoggedIn").setValue("yes");
                    loginView.showLoginSuccess();
                }else{
                    loginView.showLoginFailure();
                }
            }
        });
    }

    public void getIsLoggedInStatus(final LoginTask loginTask){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String loggedInStatus = snapshot.child("isLoggedIn").getValue().toString();
                        loginTask.onLoggedIn(loggedInStatus);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
