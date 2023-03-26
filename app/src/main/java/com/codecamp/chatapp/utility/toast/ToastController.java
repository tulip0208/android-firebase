package com.codecamp.chatapp.utility.toast;

import android.content.Context;
import android.widget.Toast;

public class ToastController {
    public void toast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
