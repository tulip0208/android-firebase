package com.codecamp.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import static com.codecamp.chatapp.R.drawable.register_btn_back;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText,passwordEditText;
    private ConstraintLayout mRelativeLayout;
    private ConstraintLayout mLoginButton;
    private FirebaseAuth mAuth;
    private TextView signUpText;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailID);
        passwordEditText = findViewById(R.id.passwordID);
        mLoginButton = findViewById(R.id.loginbuttonID);
        signUpText = findViewById(R.id.signup_nowID);
        mRelativeLayout = findViewById(R.id.relativeLayoutID);

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginButton.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, android.R.anim.fade_in));
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginProcess(email,password);
            }
        });
    }

    private void loginProcess(String email, String password)
    {
        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
        {
            emailEditText.setError("Email field are empty");
            passwordEditText.setError("Password field are empty");
        }else {
            login(email,password);
        }
    }

    private void login(String email, String password)
    {
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                    mRef.child("status").setValue("online");
                    dialog.cancel();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }else{
                    dialog.cancel();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.cancel();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRelativeLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));
    }
}