package com.codecamp.chatapp.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codecamp.chatapp.R;
import com.codecamp.chatapp.bindings.adapters.followers.FollowersAdapter;
import com.codecamp.chatapp.remote.database.follow.FirebaseFollowStrategy;
import com.codecamp.chatapp.remote.database.follow.FollowHandler;
import com.codecamp.chatapp.remote.database.follow.FollowStrategy;
import com.codecamp.chatapp.remote.database.like.FirebaseLikeStrategy;
import com.codecamp.chatapp.remote.database.like.LikeHandler;
import com.codecamp.chatapp.remote.database.like.LikeStrategy;
import com.codecamp.chatapp.remote.database.theme.FirebaseThemeStrategy;
import com.codecamp.chatapp.remote.database.theme.ThemeHandler;
import com.codecamp.chatapp.remote.database.theme.ThemeStrategy;
import com.codecamp.chatapp.remote.database.user.FirebaseUserRetriever;
import com.codecamp.chatapp.remote.error_handler.FirebaseErrorHandler;
import com.codecamp.chatapp.bindings.models.Followers;
import com.codecamp.chatapp.bindings.models.UserInfo;
import com.codecamp.chatapp.ui.dialog.bottom_dialog.BottomDialogController;
import com.codecamp.chatapp.ui.dialog.bottom_dialog.Theme;
import com.codecamp.chatapp.utility.animations.AnimationController;
import com.codecamp.chatapp.utility.background.Background;
import com.codecamp.chatapp.utility.background.BackgroundController;
import com.codecamp.chatapp.utility.constants.colors.Colors;
import com.codecamp.chatapp.utility.toast.ToastController;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private ShapeableImageView mUserImage;
    private ConstraintLayout mConstraintLayout,mOnlineIndicator,mOfflineIndicator,mChangeTheme,mFollowButton,mEmoji,likeConstraintLayout,themeIcon;
    private TextView mUserName,mUserBio,mUserEmail,mUserPhone,mFollowText,likeText;
    private ImageView mLikeIcon,mBackButton;
    private ConstraintLayout chatButton;
    private String userId;
    private FollowHandler followHandler;
    private FollowStrategy followStrategy;
    private LikeHandler likeHandler;
    private LikeStrategy likeStrategy;
    private AnimationController animationController;
    private ThemeHandler themeHandler;
    private BottomDialogController bottomDialogController;
    private ToastController toastController;
    private Colors colors;
    private BackgroundController backgroundController;
    private List<Followers> followersList;
    private FollowersAdapter followersAdapter;
    private RecyclerView followersRecyclerView;
    private FirebaseErrorHandler errorHandler;
    private FirebaseUserRetriever firebaseUserRetriever;
    private ThemeStrategy themeStrategy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profileactivity);

        firebaseUserRetriever = new FirebaseUserRetriever();
        followStrategy = new FirebaseFollowStrategy();
        followHandler = new FollowHandler(followStrategy);
        followHandler.setFollowStrategy(followStrategy);

        likeStrategy = new FirebaseLikeStrategy();
        likeHandler = new LikeHandler(likeStrategy);
        likeHandler.setLikeStrategy(likeStrategy);

        errorHandler = new FirebaseErrorHandler();

        themeStrategy = new FirebaseThemeStrategy();
        themeHandler = new ThemeHandler(themeStrategy);
        animationController = new AnimationController();
        bottomDialogController = new BottomDialogController();
        toastController = new ToastController();
        colors = new Colors();
        backgroundController = new BackgroundController();
        followersList = new ArrayList<>();
        followersAdapter = new FollowersAdapter(getApplicationContext(),followersList);

        userId = getIntent().getStringExtra("userID");

        mUserImage = findViewById(R.id.userImage);
        mUserImage.setShapeAppearanceModel(mUserImage.getShapeAppearanceModel()
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, 10)
                .setTopLeftCorner(CornerFamily.ROUNDED, 10)
                .setBottomLeftCorner(CornerFamily.ROUNDED, 10)
                .setBottomRightCorner(CornerFamily.ROUNDED, 10)
                .build());
        mConstraintLayout = findViewById(R.id.constraintLayout);
        mBackButton = findViewById(R.id.backButton);
        mFollowButton = findViewById(R.id.followConstraintLayout);
        mUserName = findViewById(R.id.userName);
        mUserBio = findViewById(R.id.userBio);
        mUserEmail = findViewById(R.id.emailID);
        mUserPhone = findViewById(R.id.phoneID);
        mFollowText = findViewById(R.id.followText);
        mOnlineIndicator = findViewById(R.id.onlineIndicator);
        mOfflineIndicator = findViewById(R.id.offlineIndicator);
        mChangeTheme = findViewById(R.id.changeThemeConstraintLayout);
        mEmoji = findViewById(R.id.emojiConstraintLayout);
        mLikeIcon = findViewById(R.id.likeIcon);
        likeConstraintLayout = findViewById(R.id.likeConstraintLayout);
        likeText = findViewById(R.id.likeText);
        themeIcon = findViewById(R.id.iconTheme);
        chatButton = findViewById(R.id.chatConstraintLayout);
        followersRecyclerView = findViewById(R.id.recyclerview);
        followersRecyclerView.setHasFixedSize(true);
        followersRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        followHandler.getFollowers(userId, new FirebaseFollowStrategy.FollowerStrategy() {
            @Override
            public void onClear() {
                followersList.clear();
            }

            @Override
            public void onFollowersFetched(String photo) {
                followersList.add(new Followers(photo));
                followersAdapter = new FollowersAdapter(ProfileActivity.this, followersList);
                followersRecyclerView.setAdapter(followersAdapter);
            }

            @Override
            public void onError(String error) {
                errorHandler.fetchError(error);
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(ProfileActivity.this,chatButton);
               Intent intent = new Intent(ProfileActivity.this, MessageActivity.class);
               intent.putExtra("userID",userId);
               startActivity(intent);
               finish();
            }
        });

        firebaseUserRetriever.retrieveUser(userId, new FirebaseUserRetriever.UserRetrievedListener() {
            @Override
            public void onUserRetrieved(UserInfo userInfo) {
                mUserName.setText(userInfo.name);
                mUserEmail.setText(userInfo.email);
                mUserPhone.setText(userInfo.phone);
                if (userInfo.bio.equals("default")) {
                    mUserBio.setText("Hi,I am using chat app");
                } else {
                    mUserBio.setText(userInfo.bio);
                }
                if(userInfo.photo.equals("default")){
                    mUserImage.setImageResource(R.drawable.avatar);
                }else{
                    Glide.with(getApplicationContext()).load(userInfo.photo).into(mUserImage);
                }

                if(userInfo.status.equals("online")){
                    mOnlineIndicator.setVisibility(View.VISIBLE);
                    mOfflineIndicator.setVisibility(View.INVISIBLE);
                }else{
                    mOnlineIndicator.setVisibility(View.INVISIBLE);
                    mOfflineIndicator.setVisibility(View.VISIBLE);
                }
            }
        });

            likeHandler.like(userId);
            likeHandler.getLikeStatus(userId, new LikeHandler.OnLikeStatus() {
                @Override
                public void onLikeStatusChanged(String likeStatus) {
                    if(likeStatus.equals("Like")){
                    mLikeIcon.setImageResource(R.drawable.outline_like_icon);
                    likeText.setText("+ "+likeStatus);
                }else{
                    mLikeIcon.setImageResource(R.drawable.filled_like_icon);
                    likeText.setText("- "+likeStatus);
                }
                }
            });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(ProfileActivity.this,mBackButton);
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        likeConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(ProfileActivity.this,likeConstraintLayout);
                likeHandler.setLikeStatus(userId);
            }
        });

        followHandler.follow(userId);
        followHandler.getFollowStatus(userId, new FollowHandler.OnFollowStatus() {
            @Override
            public void onFollowStatusChanged(String followStatus) {
                if(followStatus.equals("Follow")){
                    mFollowText.setText("+ "+followStatus);
                }else{
                    mFollowText.setText("- "+followStatus);
                }
            }
        });

        mFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(ProfileActivity.this,mFollowButton);
                followHandler.setFollowStatus(userId);
            }
        });

        themeHandler.theme(userId);
        themeHandler.getTheme(userId, new ThemeStrategy.OnTheme() {
            @Override
            public void onThemeChanged(String theme) {
                if(theme.equals("default")){
                    Background background1 = backgroundController.changeBackground(colors.DEFAULT,5);
                    Background background2 = backgroundController.changeBackground(colors.DEFAULT,999);
                    mFollowButton.setBackground(background1.drawable);
                    likeConstraintLayout.setBackground(background1.drawable);
                    mLikeIcon.setColorFilter(Color.parseColor(background1.color));
                    mBackButton.setColorFilter(Color.parseColor(background1.color));
                    chatButton.setBackground(background1.drawable);
                    themeIcon.setBackground(background2.drawable);


                }else{
                    Background background1 = backgroundController.changeBackground(theme,5);
                    Background background2 = backgroundController.changeBackground(theme,999);
                    mFollowButton.setBackground(background1.drawable);
                    likeConstraintLayout.setBackground(background1.drawable);
                    themeIcon.setBackground(background2.drawable);
                    mLikeIcon.setColorFilter(Color.parseColor(background1.color));
                    chatButton.setBackground(background1.drawable);
                    mBackButton.setColorFilter(Color.parseColor(background1.color));
                }
            }
        });

        mChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(ProfileActivity.this,mChangeTheme);
                BottomSheetDialog dialog = bottomDialogController.showBottomDialog(ProfileActivity.this);
                Theme themeLayout = bottomDialogController.provideThemes();

                themeLayout.green.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeHandler.setTheme(userId,colors.GREEN);
                        toastController.toast(ProfileActivity.this,"green theme");
                        dialog.cancel();
                    }
                });

                themeLayout.orange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeHandler.setTheme(userId,colors.ORANGE);
                        toastController.toast(ProfileActivity.this,"orange theme");
                        dialog.cancel();
                    }
                });

                themeLayout.skyBlue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeHandler.setTheme(userId,colors.SKY_BLUE);
                        toastController.toast(ProfileActivity.this,"sky blue theme");
                        dialog.cancel();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        animationController.slideInLeftAnim(ProfileActivity.this,mConstraintLayout);
    }

}