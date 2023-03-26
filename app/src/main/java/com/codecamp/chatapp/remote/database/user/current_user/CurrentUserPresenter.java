package com.codecamp.chatapp.remote.database.user.current_user;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.codecamp.chatapp.remote.database.user.current_user.followers_handler.FollowersHandler;
import com.codecamp.chatapp.remote.database.user.current_user.followers_handler.FollowersTask;
import com.codecamp.chatapp.remote.database.user.current_user.image_uploader.ImageUploader;
import com.codecamp.chatapp.remote.database.user.current_user.image_uploader.UploadImageTask;
import com.codecamp.chatapp.remote.database.user.current_user.like_handler.LikesHandler;
import com.codecamp.chatapp.remote.database.user.current_user.like_handler.LikesTask;
import com.codecamp.chatapp.remote.database.user.current_user.updatehandler.UpdateHandler;
import com.codecamp.chatapp.remote.database.user.current_user.updatehandler.UpdateTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class CurrentUserPresenter {
    private CurrentUserHelper currentPersonHelper;
    private UpdateHandler updateHandler;
    private ImageUploader imageUploader;
    private FollowersHandler followersHandler;
    private LikesHandler likesHandler;
    private String userId;
    public CurrentUserPresenter(){
        try{
            currentPersonHelper = new CurrentUserHelper();
            userId = currentPersonHelper.getUserId();
            updateHandler = new UpdateHandler();
            imageUploader = new ImageUploader();
            followersHandler = new FollowersHandler();
            likesHandler = new LikesHandler();
        }catch (Exception e){

        }

    }
    public void fetchCurrentUser( final OnFetchCurrentUser onFetchCurrentUser){
        currentPersonHelper.fetchCurrentPerson(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String image = snapshot.child("image").getValue().toString();
                String status = snapshot.child("status").getValue().toString();
                String bio = snapshot.child("bio").getValue().toString();
                String timestamp = snapshot.child("time_stamp").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String about = snapshot.child("about").getValue().toString();
                onFetchCurrentUser.onFetched(new CurrentUser(name,email,image,status,bio,timestamp,phone,about));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onFetchCurrentUser.onFetchFailed(error.getMessage());
            }
        });
    }
    public void disconnectUser(){
        currentPersonHelper.disconnectUser();
    }
    public void disconnectResult(String status){
        currentPersonHelper.disconnectResult(status);
    }
    public void connectResult(String status){
        currentPersonHelper.connectResult(status);
    }
    public String getUserId(){
        return userId;
    }
    public void updateName(String name, final UpdateTask updateTask){
        updateTask.onUpdating();
        updateHandler.updateName(name, new
                OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            updateTask.onSuccess("updated successfully");
                        }else{
                            updateTask.onFailure("Failed!!");
                        }
                    }
                }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                updateTask.onDatabaseError(e.getMessage().toString());
            }
        });
    }

    public void updateBio(String bio, final UpdateTask updateTask){
        updateTask.onUpdating();
        updateHandler.updateBio(bio, new
                OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            updateTask.onSuccess("updated successfully");
                        }else{
                            updateTask.onFailure("Failed!!");
                        }
                    }
                }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                updateTask.onDatabaseError(e.getMessage().toString());
            }
        });
    }

    public void updateAbout(String about, final UpdateTask updateTask){
        updateTask.onUpdating();
        updateHandler.updateAbout(about, new
                OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            updateTask.onSuccess("updated successfully");
                        }else{
                            updateTask.onFailure("Failed!!");
                        }
                    }
                }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                updateTask.onDatabaseError(e.getMessage().toString());
            }
        });
    }

    public void updatePhone(String phone, final UpdateTask updateTask){
        updateTask.onUpdating();
        updateHandler.updatePhone(phone, new
                OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            updateTask.onSuccess("updated successfully");
                        }else{
                            updateTask.onFailure("Failed!!");
                        }
                    }
                }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                updateTask.onDatabaseError(e.getMessage().toString());
            }
        });
    }

    public void uploadImage(Uri imageUri, final UploadImageTask uploadImageTask){
        imageUploader.uploadImage(imageUri,uploadImageTask);
    }
    public void getFollowersCount(final FollowersTask followersTask){
        followersHandler.getFollowersCount(followersTask);
    }
    public void getLikesCount(final LikesTask likesTask){
        likesHandler.getLikesCount(likesTask);
    }
}
