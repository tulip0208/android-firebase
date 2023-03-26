package com.codecamp.chatapp.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codecamp.chatapp.R;
import com.codecamp.chatapp.remote.auth.logout.LogoutHandler;
import com.codecamp.chatapp.remote.auth.logout.LogoutTask;
import com.codecamp.chatapp.remote.database.user.current_user.CurrentUser;
import com.codecamp.chatapp.remote.database.user.current_user.CurrentUserPresenter;
import com.codecamp.chatapp.remote.database.user.current_user.OnFetchCurrentUser;
import com.codecamp.chatapp.remote.database.user.current_user.followers_handler.FollowersTask;
import com.codecamp.chatapp.remote.database.user.current_user.image_uploader.UploadImageTask;
import com.codecamp.chatapp.remote.database.user.current_user.like_handler.LikesTask;
import com.codecamp.chatapp.remote.error_handler.FirebaseErrorHandler;
import com.codecamp.chatapp.ui.dialog.menu_dialog.MenuPopup;
import com.codecamp.chatapp.ui.dialog.progress_dialog.ProgressDialogHandler;
import com.codecamp.chatapp.utility.animations.AnimationController;
import com.codecamp.chatapp.utility.constants.texts.Texts;
import com.codecamp.chatapp.utility.toast.ToastController;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;

import java.util.List;

public class CurrentUserActivity extends AppCompatActivity{
    private ShapeableImageView userImage,image1,image2;
    private ImageView backIcon,menuIcon,imagePlusButton;
    private TextView userName,userBio,followCount,likeCount,userAbout,userEmail,userPhone;
    private ConstraintLayout plusPhotosLayout,editLayout;
    private ScrollView scrollView;
    private AnimationController animationController;
    private CurrentUserPresenter currentPersonPresenter;
    private FirebaseErrorHandler firebaseErrorHandler;
    private Texts texts;
    private ToastController toastController;
    private ProgressDialogHandler progressDialogHandler;
    private MenuPopup menuPopup;
    private LogoutHandler logoutHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user);

        currentPersonPresenter = new CurrentUserPresenter();
        firebaseErrorHandler = new FirebaseErrorHandler();
        animationController = new AnimationController();
        logoutHandler = new LogoutHandler();
        texts = new Texts();
        toastController = new ToastController();
        progressDialogHandler = new ProgressDialogHandler(this);
        menuPopup = new MenuPopup();

        userName = findViewById(R.id.USER_NAME);
        userBio = findViewById(R.id.USER_BIO);
        userEmail = findViewById(R.id.EMAIL);
        userPhone = findViewById(R.id.PHONE);
        likeCount = findViewById(R.id.likeCount);
        followCount = findViewById(R.id.followerCount);
        userAbout = findViewById(R.id.ABOUT);
        userImage = findViewById(R.id.userImage);
        image1 = findViewById(R.id.IMAGE1);
        image2 = findViewById(R.id.IMAGE2);
        backIcon = findViewById(R.id.backIcon);
        plusPhotosLayout = findViewById(R.id.PLUS_PHOTOS_LAYOUT);
        scrollView = findViewById(R.id.SCROLL_VIEW);
        editLayout = findViewById(R.id.EDIT_LAYOUT);
        menuIcon = findViewById(R.id.menuIcon);
        imagePlusButton = findViewById(R.id.IMAGE_PLUS);

        userImage.setShapeAppearanceModel(userImage.getShapeAppearanceModel()
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, 20)
                .setTopLeftCorner(CornerFamily.ROUNDED, 20)
                .setBottomLeftCorner(CornerFamily.ROUNDED, 20)
                .setBottomRightCorner(CornerFamily.ROUNDED, 20)
                .build());
        image1.setShapeAppearanceModel(image1.getShapeAppearanceModel()
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, 10)
                .setTopLeftCorner(CornerFamily.ROUNDED, 10)
                .setBottomLeftCorner(CornerFamily.ROUNDED, 10)
                .setBottomRightCorner(CornerFamily.ROUNDED, 10)
                .build());
        image2.setShapeAppearanceModel(image2.getShapeAppearanceModel()
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, 10)
                .setTopLeftCorner(CornerFamily.ROUNDED, 10)
                .setBottomLeftCorner(CornerFamily.ROUNDED, 10)
                .setBottomRightCorner(CornerFamily.ROUNDED, 10)
                .build());

        currentPersonPresenter.getFollowersCount(new FollowersTask() {
            @Override
            public void onTotalFollowers(List<String> totalFollowers) {
                String count = String.valueOf(totalFollowers.size());
                followCount.setText(count);

            }
        });

        currentPersonPresenter.getLikesCount(new LikesTask() {
            @Override
            public void onTotalLikes(List<String> likesList) {
                String count = String.valueOf(likesList.size());
                likeCount.setText(count);
            }
        });
        currentPersonPresenter.fetchCurrentUser(new OnFetchCurrentUser() {
            @Override
            public void onFetched(CurrentUser currentUser) {
                userName.setText(currentUser.getName());
                if(currentUser.getBio().equals("default")){
                    userBio.setText(texts.BIO_DEFAULT);
                }else{
                    userBio.setText(currentUser.getBio());
                }
                userEmail.setText(currentUser.getEmail());
                userPhone.setText(currentUser.getPhone());
                if(currentUser.getAbout().equals("default")){
                    userAbout.setText(texts.ABOUT_DEFAULT);
                }else{
                    userAbout.setText(currentUser.getAbout());
                }
                if(currentUser.getImage().equals("default")){
                    userImage.setImageResource(R.drawable.avatar);
                }else{
                    Glide.with(CurrentUserActivity.this).load(currentUser.getImage()).into(userImage);
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
                animationController.fadeInAnimation(CurrentUserActivity.this,backIcon);
                startActivity(new Intent(CurrentUserActivity.this,MainActivity.class));
                finish();
            }
        });
        plusPhotosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(CurrentUserActivity.this,plusPhotosLayout);
            }
        });
        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(CurrentUserActivity.this,editLayout);
                startActivity(new Intent(CurrentUserActivity.this,SettingActivity.class));
                finish();
            }
        });
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(CurrentUserActivity.this,menuIcon);
                menuPopup.showMenuPopup(CurrentUserActivity.this, menuIcon, new MenuPopup.PopupMenuTask() {
                    @Override
                    public void onTask() {
                        logoutHandler.logout(new LogoutTask() {
                            @Override
                            public void onSuccess(String message) {
                                toastController.toast(CurrentUserActivity.this,message);
                            }

                            @Override
                            public void onFailure(String error) {
                                firebaseErrorHandler.logError(error);
                            }
                        });
                    }
                });
            }
        });

        imagePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(CurrentUserActivity.this,imagePlusButton);
                updateUserImage();
            }
        });

    }


    private void updateUserImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, texts.REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == texts.REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                currentPersonPresenter.uploadImage(imageUri, new UploadImageTask() {
                    @Override
                    public void onUploading(String message) {
                        progressDialogHandler.showDialog();
                    }

                    @Override
                    public void onUploaded(String message) {
                        progressDialogHandler.dismissDialog();
                        toastController.toast(CurrentUserActivity.this,message);
                    }

                    @Override
                    public void onFailed(String error) {
                        progressDialogHandler.dismissDialog();
                        firebaseErrorHandler.uploadFileError(error);
                    }
                });


            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        animationController.slideInLeftAnim(CurrentUserActivity.this,scrollView);
    }

}