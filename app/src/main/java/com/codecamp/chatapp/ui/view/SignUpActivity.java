package com.codecamp.chatapp.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.codecamp.chatapp.R;
import com.codecamp.chatapp.remote.auth.signup.SignupView;
import com.codecamp.chatapp.remote.auth.signup.signuppresenter.FirebaseSignupPresenter;
import com.codecamp.chatapp.ui.dialog.progress_dialog.ProgressDialogHandler;
import com.codecamp.chatapp.utility.animations.AnimationController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SignUpActivity extends AppCompatActivity implements SignupView {
    private EditText nameEditText,phoneEditText, emailEditText, passwordEditText;
    private ConstraintLayout mSignupButton,mProgressLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef,mStatusRef,mFollowerRef;
    private TextView loginText;
    private ConstraintLayout mConstraintLayout;
    private FirebaseSignupPresenter firebaseSignupPresenter;
    private ProgressDialogHandler progressDialogHandler;
    private AnimationController animationController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        firebaseSignupPresenter = new FirebaseSignupPresenter(this);
        progressDialogHandler = new ProgressDialogHandler(SignUpActivity.this);
        animationController = new AnimationController();

        nameEditText = findViewById(R.id.nameID);
        phoneEditText = findViewById(R.id.phoneID);
        emailEditText = findViewById(R.id.emailID);
        passwordEditText = findViewById(R.id.passwordID);
        mSignupButton = findViewById(R.id.signupbuttonID);
        loginText = findViewById(R.id.login_nowID);
        mConstraintLayout = findViewById(R.id.relativeLayoutID);
        mProgressLayout = findViewById(R.id.progressLayout);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(SignUpActivity.this,loginText);
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(SignUpActivity.this,mSignupButton);
                String  name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                signupProcess(name,phone,email,password);

            }
        });
    }

    private void signupProcess(String name,String phone ,String email, String password)
    {

            if(TextUtils.isEmpty(name) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password) )
            {

                nameEditText.setError("Name field are empty");
                phoneEditText.setError("Phone field are empty");
                emailEditText.setError("Email field are empty");
                passwordEditText.setError("Password field are empty");

            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailEditText.setError("wrong email pattern");
            }else if(!android.util.Patterns.PHONE.matcher(phone).matches()){
                phoneEditText.setError("invalid phone number");
            }else if(password.length()<6){
                passwordEditText.setError("password must be 7 or above ");
            }else {
                //createUser(name,phone,email,password);
                progressDialogHandler.showDialog();
                firebaseSignupPresenter.signup(name,email,password,phone);

            }

    }

    private void createUser(String name,String phone, String email, String password)
    {
//        Dialog dialog = new Dialog(SignUpActivity.this);
//        dialog.setContentView(R.layout.progress_dialog);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.show();
        //mProgressLayout.setVisibility(View.VISIBLE);

//        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful())
//                {
//
//                    mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
//
//                    HashMap<String,Object> hashMap = new HashMap<>();
//                    hashMap.put("name",name);
//                    hashMap.put("phone",phone);
//                    hashMap.put("image","default");
//                    hashMap.put("email",email);
//                    hashMap.put("password",password);
//                    hashMap.put("bio","default");
//                    hashMap.put("status","offline");
//                    hashMap.put("typingTo","noOne");
//                    hashMap.put("time_stamp", "default");
//                    hashMap.put("message_sender","default");
//
//
//                    mRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful())
//                            {
//                                //dialog.cancel();
//                                //mProgressLayout.setVisibility(View.INVISIBLE);
//                                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
//                                finish();
//                            }else{
//                                //dialog.cancel();
//                                //mProgressLayout.setVisibility(View.INVISIBLE);
//                            }
//                        }
//                    });
//
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                //dialog.cancel();
//                //mProgressLayout.setVisibility(View.INVISIBLE);
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));
    }

    @Override
    public void showSignupSuccess() {
        progressDialogHandler.dismissDialog();
        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showSignupFailure() {
        progressDialogHandler.dismissDialog();
    }
}