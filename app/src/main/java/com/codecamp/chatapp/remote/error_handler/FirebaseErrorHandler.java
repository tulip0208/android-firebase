package com.codecamp.chatapp.remote.error_handler;

import android.app.Application;

import com.codecamp.chatapp.utility.toast.ToastController;

public class FirebaseErrorHandler extends Application {
    private ToastController toastController;
    public FirebaseErrorHandler(){
        toastController = new ToastController();
    }
    public void signupError(String error){
        toastController.toast(this,error);
    }
    public void usersFetchingError(String error){
        toastController.toast(this,error);
    }
    public void currentUserFetchingError(String error){
        toastController.toast(this,error);
    }
    public void updateError(String error){
        toastController.toast(this,error);
    }
    public void uploadFileError(String error){
        toastController.toast(this,error);
    }
    public void logError(String error){
        toastController.toast(this,error);
    }

    public void fetchError(String error){
        toastController.toast(this,error);
    }
}
