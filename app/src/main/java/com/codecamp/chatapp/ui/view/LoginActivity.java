package com.codecamp.chatapp.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.codecamp.chatapp.R;
import com.codecamp.chatapp.remote.auth.login.LoginView;
import com.codecamp.chatapp.remote.auth.login.loginpresenter.FirebaseLoginPresenter;
import com.codecamp.chatapp.ui.dialog.progress_dialog.ProgressDialogHandler;
import com.codecamp.chatapp.utility.animations.AnimationController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText,passwordEditText;
    private ConstraintLayout mRelativeLayout;
    private ConstraintLayout mLoginButton;
    private FirebaseAuth mAuth;
    private TextView signUpText;
    private DatabaseReference mRef;
    private FirebaseLoginPresenter firebaseLoginPresenter;
    private ProgressDialogHandler progressDialogHandler;
    private AnimationController animationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        firebaseLoginPresenter = new FirebaseLoginPresenter();
        progressDialogHandler = new ProgressDialogHandler(LoginActivity.this);
        animationController = new AnimationController();
        emailEditText = findViewById(R.id.emailID);
        passwordEditText = findViewById(R.id.passwordID);
        mLoginButton = findViewById(R.id.loginbuttonID);
        signUpText = findViewById(R.id.signup_nowID);
        mRelativeLayout = findViewById(R.id.relativeLayoutID);

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(LoginActivity.this,signUpText);
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(LoginActivity.this,mLoginButton);
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
            progressDialogHandler.showDialog();
            firebaseLoginPresenter.login(email, password, new LoginView() {
                @Override
                public void showLoginSuccess() {
                    progressDialogHandler.dismissDialog();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void showLoginFailure() {
                    progressDialogHandler.dismissDialog();
                }

                @Override
                public void onLoggedIn(String loggedInStatus) {

                }
            });
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        animationController.slideInLeftAnim(LoginActivity.this,mRelativeLayout);
    }

}