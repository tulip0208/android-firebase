package com.codecamp.chatapp;

import static com.codecamp.chatapp.R.drawable.change_theme_back;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profileactivity extends AppCompatActivity {
    private ShapeableImageView mUserImage;
    private ConstraintLayout mConstraintLayout,mBackButton,mOnlineIndicator,mOfflineIndicator,mChangeTheme,mEmoji,mFollowButton;
    private TextView mUserName,mUserBio,mUserNameWithSmallLetter,mUserEmail,mUserPhone,mFollowText;
    private ImageView mLikeIcon;
    private DatabaseReference mUserRef,mFollowRef,mCurrentUserRef;
    private FirebaseAuth mUserAuth,mCurrentUserAuth;
    private String userId;
    private String currentUserName,currentUserImage,currentUserBio,currentUserStatus,currentUserEmail,currentUserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profileactivity);

        userId = getIntent().getStringExtra("userID");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mCurrentUserAuth = FirebaseAuth.getInstance();
        mCurrentUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUserAuth.getCurrentUser().getUid());

        mUserImage = findViewById(R.id.userImage);
        mUserImage.setShapeAppearanceModel(mUserImage.getShapeAppearanceModel()
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, 30)
                .setTopLeftCorner(CornerFamily.ROUNDED, 30)
                .setBottomLeftCorner(CornerFamily.ROUNDED, 30)
                .setBottomRightCorner(CornerFamily.ROUNDED, 30)
                .build());
        mConstraintLayout = findViewById(R.id.constraintLayout);
        mBackButton = findViewById(R.id.backButton);
        mFollowButton = findViewById(R.id.followConstraintLayout);
        mUserName = findViewById(R.id.userName);
        mUserBio = findViewById(R.id.userBio);
        mUserNameWithSmallLetter = findViewById(R.id.userNameID);
        mUserEmail = findViewById(R.id.emailID);
        mUserPhone = findViewById(R.id.phoneID);
        mFollowText = findViewById(R.id.followText);
        mOnlineIndicator = findViewById(R.id.onlineIndicator);
        mOfflineIndicator = findViewById(R.id.offlineIndicator);
        mChangeTheme = findViewById(R.id.changeThemeConstraintLayout);
        mEmoji = findViewById(R.id.emojiConstraintLayout);
        mLikeIcon = findViewById(R.id.likeIcon);

        mCurrentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserName = snapshot.child("name").getValue().toString();
                currentUserImage = snapshot.child("image").getValue().toString();
                currentUserBio = snapshot.child("bio").getValue().toString();
                currentUserEmail = snapshot.child("email").getValue().toString();
                currentUserStatus = snapshot.child("status").getValue().toString();
                currentUserPhone = snapshot.child("phone").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackButton.startAnimation(AnimationUtils.loadAnimation(Profileactivity.this, R.anim.fade_in_animation));
                startActivity(new Intent(Profileactivity.this, MainActivity.class));
                finish();
            }
        });

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = snapshot.child("name").getValue().toString();
                String image = snapshot.child("image").getValue().toString();
                String bio = snapshot.child("bio").getValue().toString();
                String status = snapshot.child("status").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String followStatus = snapshot.child("isFollow").getValue().toString();
                String islikeStatus = snapshot.child("isLike").getValue().toString();

                mUserName.setText(name);
                mFollowText.setText(followStatus);
                mFollowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFollowButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in_animation));
                        if(followStatus.equals("Follow")){
                            mUserRef.child("isFollow").setValue("Following");
                            mFollowText.setText(followStatus);
                            HashMap<String,String> mCurrentUserMap = new HashMap<>();
                            mCurrentUserMap.put("name",currentUserName);
                            mCurrentUserMap.put("email",currentUserEmail);
                            mCurrentUserMap.put("phone",currentUserPhone);
                            mCurrentUserMap.put("bio",currentUserBio);
                            mCurrentUserMap.put("status",currentUserStatus);
                            mCurrentUserMap.put("image",currentUserImage);
                            mUserRef.child("follower").child(mCurrentUserAuth.getCurrentUser().getUid()).setValue(mCurrentUserMap);
                        }else{
                            mUserRef.child("isFollow").setValue("Follow");
                            mFollowText.setText(followStatus);
                        }

                    }
                });

                if(islikeStatus.equals("Like")){
                    mLikeIcon.setImageResource(R.drawable.outline_like_icon);
                }else{
                    mLikeIcon.setImageResource(R.drawable.filled_like_icon);
                }

                mLikeIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLikeIcon.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in_animation));
                        if(islikeStatus.equals("Like")){
                            mUserRef.child("isLike").setValue("Liked");
                        }else{
                            mUserRef.child("isLike").setValue("Like");
                        }

                    }
                });

                if (image.equals("default")) {
                    mUserImage.setImageResource(R.drawable.avatar);
                } else {
                    Glide.with(getApplicationContext()).load(image).into(mUserImage);
                }

                mUserNameWithSmallLetter.setText("@" + name.toLowerCase());

                if (bio.equals("default")) {
                    mUserBio.setText("Hi,I am using chat app");
                } else {
                    mUserBio.setText(bio);
                }

                mUserEmail.setText(email);
                mUserPhone.setText(phone);

                if(status.equals("online")){
                    mOnlineIndicator.setVisibility(View.VISIBLE);
                    mOfflineIndicator.setVisibility(View.INVISIBLE);
                }else{
                    mOnlineIndicator.setVisibility(View.INVISIBLE);
                    mOfflineIndicator.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChangeTheme.startAnimation(AnimationUtils.loadAnimation(Profileactivity.this,R.anim.fade_in_animation));
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Profileactivity.this);
                bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                bottomSheetDialog.setContentView(R.layout.change_theme);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
                bottomSheetDialog.show();
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left));
    }
}