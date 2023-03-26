package com.codecamp.chatapp.ui.dialog.progress_dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;

import com.codecamp.chatapp.R;

public class ProgressDialogHandler {
    private Dialog dialog;
    public ProgressDialogHandler(Context context){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void showDialog(){
        dialog.show();
    }

    public void dismissDialog(){
        dialog.cancel();
    }
}
