package com.codecamp.chatapp.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codecamp.chatapp.R;
import com.codecamp.chatapp.remote.database.user.current_user.CurrentUser;
import com.codecamp.chatapp.remote.database.user.current_user.CurrentUserPresenter;
import com.codecamp.chatapp.remote.database.user.current_user.OnFetchCurrentUser;
import com.codecamp.chatapp.remote.database.user.current_user.updatehandler.UpdateTask;
import com.codecamp.chatapp.remote.error_handler.FirebaseErrorHandler;
import com.codecamp.chatapp.ui.dialog.progress_dialog.ProgressDialogHandler;
import com.codecamp.chatapp.ui.dialog.update_current_user_dialog.UpdateCurrentUserDialog;
import com.codecamp.chatapp.utility.animations.AnimationController;
import com.codecamp.chatapp.utility.constants.texts.Texts;
import com.codecamp.chatapp.utility.toast.ToastController;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    private ImageView backIcon,imagePlusButton;
    private ImageView updateNameIcon,updateBioIcon,updatePhoneIcon,updateAboutIcon;
    private CircleImageView userImage;
    private TextView userName,userAbout,userBio,userPhone;
    private AnimationController animationController;
    private ToastController toastController;
    private CurrentUserPresenter currentPersonPresenter;
    private FirebaseErrorHandler firebaseErrorHandler;
    private Texts texts;
    private UpdateCurrentUserDialog currentUserDialog;
    private ProgressDialogHandler progressDialogHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        animationController = new AnimationController();
        currentPersonPresenter = new CurrentUserPresenter();
        firebaseErrorHandler = new FirebaseErrorHandler();
        texts = new Texts();
        toastController = new ToastController();
        currentUserDialog = new UpdateCurrentUserDialog(SettingActivity.this);
        progressDialogHandler = new ProgressDialogHandler(SettingActivity.this);

        constraintLayout = findViewById(R.id.constraintLayout);

        backIcon = findViewById(R.id.backIcon);
        userImage = findViewById(R.id.USER_IMAGE);
        imagePlusButton = findViewById(R.id.IMAGE_PLUS);
        userName = findViewById(R.id.NAME);
        userAbout = findViewById(R.id.ABOUT);
        userBio = findViewById(R.id.BIO);
        userPhone = findViewById(R.id.PHONE);

        updateNameIcon = findViewById(R.id.NAME_UPDATE_ICON);
        updateBioIcon = findViewById(R.id.BIO_UPDATE_ICON);
        updatePhoneIcon = findViewById(R.id.PHONE_UPDATE_ICON);
        updateAboutIcon = findViewById(R.id.ABOUT_UPDATE_ICON);

        currentPersonPresenter.fetchCurrentUser(new OnFetchCurrentUser() {
            @Override
            public void onFetched(CurrentUser currentUser) {
                userName.setText(currentUser.getName());
                if(currentUser.getBio().equals("default")){
                    userBio.setText(texts.BIO_DEFAULT);
                }else{
                    userBio.setText(currentUser.getBio());
                }
                userPhone.setText(currentUser.getPhone());
                if(currentUser.getAbout().equals("default")){
                    userAbout.setText(texts.ABOUT_DEFAULT);
                }else{
                    userAbout.setText(currentUser.getAbout());
                }
                if(currentUser.getImage().equals("default")){
                    userImage.setImageResource(R.drawable.avatar);
                }else{
                    Glide.with(SettingActivity.this).load(currentUser.getImage()).into(userImage);
                }
            }

            @Override
            public void onFetchFailed(String error) {
                firebaseErrorHandler.currentUserFetchingError(error);
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(SettingActivity.this,backIcon);
                startActivity(new Intent(SettingActivity.this, CurrentUserActivity.class));
                finish();
            }
        });

        imagePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(SettingActivity.this,imagePlusButton);
            }
        });

        updateNameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(SettingActivity.this,updateNameIcon);
                currentUserDialog.update("name",new UpdateTask() {
                    @Override
                    public void onUpdating() {
                        progressDialogHandler.showDialog();
                        currentUserDialog.dismissDialog();
                    }

                    @Override
                    public void onSuccess(String message) {
                        progressDialogHandler.dismissDialog();
                        toastController.toast(SettingActivity.this,message);
                    }

                    @Override
                    public void onFailure(String error) {
                        progressDialogHandler.dismissDialog();
                        firebaseErrorHandler.updateError(error);
                    }

                    @Override
                    public void onDatabaseError(String error) {
                        progressDialogHandler.dismissDialog();
                        firebaseErrorHandler.updateError(error);
                    }
                });
                currentUserDialog.showDialog();
            }
        });

        updateBioIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(SettingActivity.this,updateNameIcon);
                currentUserDialog.update("bio",new UpdateTask() {
                    @Override
                    public void onUpdating() {
                        progressDialogHandler.showDialog();
                        currentUserDialog.dismissDialog();
                    }

                    @Override
                    public void onSuccess(String message) {
                        progressDialogHandler.dismissDialog();
                        toastController.toast(SettingActivity.this,message);
                    }

                    @Override
                    public void onFailure(String error) {
                        progressDialogHandler.dismissDialog();
                        firebaseErrorHandler.updateError(error);
                    }

                    @Override
                    public void onDatabaseError(String error) {
                        progressDialogHandler.dismissDialog();
                        firebaseErrorHandler.updateError(error);
                    }
                });
                currentUserDialog.showDialog();
            }
        });

        updatePhoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(SettingActivity.this,updateNameIcon);
                currentUserDialog.update("phone",new UpdateTask() {
                    @Override
                    public void onUpdating() {
                        progressDialogHandler.showDialog();
                        currentUserDialog.dismissDialog();
                    }

                    @Override
                    public void onSuccess(String message) {
                        progressDialogHandler.dismissDialog();
                        toastController.toast(SettingActivity.this,message);
                    }

                    @Override
                    public void onFailure(String error) {
                        progressDialogHandler.dismissDialog();
                        firebaseErrorHandler.updateError(error);
                    }

                    @Override
                    public void onDatabaseError(String error) {
                        progressDialogHandler.dismissDialog();
                        firebaseErrorHandler.updateError(error);
                    }
                });
                currentUserDialog.showDialog();
            }
        });

        updateAboutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(SettingActivity.this,updateNameIcon);
                currentUserDialog.update("about",new UpdateTask() {
                    @Override
                    public void onUpdating() {
                        progressDialogHandler.showDialog();
                        currentUserDialog.dismissDialog();
                    }

                    @Override
                    public void onSuccess(String message) {
                        progressDialogHandler.dismissDialog();
                        toastController.toast(SettingActivity.this,message);
                    }

                    @Override
                    public void onFailure(String error) {
                        progressDialogHandler.dismissDialog();
                        firebaseErrorHandler.updateError(error);
                    }

                    @Override
                    public void onDatabaseError(String error) {
                        progressDialogHandler.dismissDialog();
                        firebaseErrorHandler.updateError(error);
                    }
                });
                currentUserDialog.showDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        animationController.slideInLeftAnim(this,constraintLayout);
    }
}