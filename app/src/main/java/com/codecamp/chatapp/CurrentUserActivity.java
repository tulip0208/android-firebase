package com.codecamp.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codecamp.chatapp.dapter.FollowerAdapter;
import com.codecamp.chatapp.model.Follower;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrentUserActivity extends AppCompatActivity {
    private ConstraintLayout mConstraintLayout,mBackButton,mNameConstraintLayout,mSettingConstraintLayout,mUserPhoneConstraintLayout;
    private CircleImageView mUserImage;
    private TextView mUserName,mUserNameWithSmallLetter,mUserBio,mUserEmail,mUserPhone;
    private RecyclerView mFollowerRecyclerView;
    private FollowerAdapter mFollowerAdapter;
    private ArrayList<Follower> mFollowerList;
    private FirebaseAuth mUserAuth;
    private DatabaseReference mUserRef;
    private StorageReference mImageRef;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_current_user);

        mUserAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserAuth.getCurrentUser().getUid());
        mImageRef = FirebaseStorage.getInstance().getReference().child("PersonImage").child(mUserAuth.getCurrentUser().getUid());
        mFollowerRecyclerView = findViewById(R.id.followerRecyclerView);

        mConstraintLayout = findViewById(R.id.constraintLayout);
        mBackButton = findViewById(R.id.backButton);
        mNameConstraintLayout = findViewById(R.id.nameConstraintLayout);
        mSettingConstraintLayout = findViewById(R.id.userSettingConstraintLayout);
        mUserImage = findViewById(R.id.userImage);
        mUserName = findViewById(R.id.userName);
        mUserNameWithSmallLetter = findViewById(R.id.userNameID);
        mUserBio = findViewById(R.id.bioID);
        mUserEmail = findViewById(R.id.emailID);
        mUserPhone = findViewById(R.id.phoneID);
        mUserPhoneConstraintLayout = findViewById(R.id.userPhoneConstraintLayout);

        //follower portion
        mFollowerRecyclerView.setHasFixedSize(true);
        mFollowerRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        mFollowerList = new ArrayList<>();
        mUserRef.child("follower").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    mFollowerList.add(new Follower(name,image,dataSnapshot.getKey()));
                }
                mFollowerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mFollowerAdapter = new FollowerAdapter(getApplicationContext(),mFollowerList);
        mFollowerRecyclerView.setAdapter(mFollowerAdapter);


        //follower portion

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String image = snapshot.child("image").getValue().toString();
                String bio = snapshot.child("bio").getValue().toString();
                mUserName.setText(name);
                mUserNameWithSmallLetter.setText("@" + name.toLowerCase());
                mUserEmail.setText(email);
                if (bio.equals("default")) {
                    mUserBio.setText("Hey I'm using chat app");
                } else {
                    mUserBio.setText(bio);
                }
                mUserPhone.setText(phone);
                if (image.equals("default")) {
                    mUserImage.setImageResource(R.drawable.avatar);
                } else {
                    Glide.with(getApplicationContext()).load(image).into(mUserImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation));
                startActivity(new Intent(CurrentUserActivity.this, MainActivity.class));
                finish();
            }
        });


        mNameConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNameConstraintLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation));
                updateUserPhoto();

            }
        });


        mSettingConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingConstraintLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation));
                Intent intent = new Intent(CurrentUserActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


        mUserPhoneConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserPhoneConstraintLayout.startAnimation(AnimationUtils.loadAnimation(CurrentUserActivity.this, R.anim.fade_in_animation));
                mUserRef.child("phone").setValue("015XXXXXXXXXX");
            }
        });

    }


private void updateUserPhoto(){
    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
    galleryIntent.setType("image/*");
    startActivityForResult(galleryIntent,1);
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Dialog dialog = new Dialog(CurrentUserActivity.this);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
            mImageUri = data.getData();
            if(mImageUri != null){
                StorageReference filePath = mImageRef.child(mImageUri.getLastPathSegment());
                final UploadTask uploadTask = filePath.putFile(mImageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful())
                                   {

                                        throw task.getException();
                                    }
                                    // downloadImageUrl = filePath.getDownloadUrl().toString();
                                    return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    Uri downloadUri = task.getResult();
                                    String downloadImageUrl=  downloadUri.toString();
                                    mUserRef.child("image").setValue(downloadImageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                dialog.cancel();
                                                Toast.makeText(getApplicationContext(),"image uploaded successfully",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.cancel();
                    }
                });
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
    }


}