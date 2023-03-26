package com.codecamp.chatapp.remote.auth.signup.signuppresenter;

import androidx.annotation.NonNull;

import com.codecamp.chatapp.remote.auth.signup.SignupView;
import com.codecamp.chatapp.remote.auth.signup.handlesignupsuccess.HandleSignup;
import com.codecamp.chatapp.remote.auth.signup.signuphelper.FirebaseSignupHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class FirebaseSignupPresenter {
    private SignupView signupView;
    private FirebaseSignupHelper firebaseSignupHelper;
    private HandleSignup handleSignup;

    public FirebaseSignupPresenter(SignupView signupView){
        this.signupView = signupView;
        firebaseSignupHelper = new FirebaseSignupHelper();
        handleSignup = new HandleSignup(signupView);
    }

    public void signup(String name,String email,String password,String phone){
        firebaseSignupHelper.createUserWithEmailAndPassword(email, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    handleSignup.saveData(name,email,phone,password);
                }else {
                    handleSignup.dataSaveFailed();
                }
            }
        });
    }
}
