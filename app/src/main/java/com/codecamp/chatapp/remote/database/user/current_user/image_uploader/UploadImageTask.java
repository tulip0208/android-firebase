package com.codecamp.chatapp.remote.database.user.current_user.image_uploader;

public interface UploadImageTask {
    void onUploading(String message);
    void onUploaded(String message);
    void onFailed(String error);
}
