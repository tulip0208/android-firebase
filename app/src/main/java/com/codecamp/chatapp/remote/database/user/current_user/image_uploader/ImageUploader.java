package com.codecamp.chatapp.remote.database.user.current_user.image_uploader;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageUploader {
    public void uploadImage(Uri imageUri,final UploadImageTask uploadImageTask){
        uploadImageTask.onUploading("uploading....");
        StorageReference imageRef = FirebaseStorage
                .getInstance()
                .getReference()
                .child("Images")
                .child(imageUri.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.continueWithTask(task -> {
            if(!task.isSuccessful()){
                throw task.getException();
            }
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    saveImage(task.getResult().toString(),uploadImageTask);
                }
            }
        });
    }

    public void saveImage(String imageUrl,final UploadImageTask uploadImageTask){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("image")
                .setValue(imageUrl)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            uploadImageTask.onUploaded("uploaded successfully");
                        }else{
                            uploadImageTask.onFailed("oops!failed to upload!");
                        }
                    }
                }).addOnFailureListener(e -> {
                        uploadImageTask.onFailed(e.getMessage().toString());
        });
    }
}
