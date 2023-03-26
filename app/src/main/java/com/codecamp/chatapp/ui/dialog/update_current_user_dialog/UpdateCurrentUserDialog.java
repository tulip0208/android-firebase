package com.codecamp.chatapp.ui.dialog.update_current_user_dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.codecamp.chatapp.R;
import com.codecamp.chatapp.remote.database.user.current_user.CurrentUserPresenter;
import com.codecamp.chatapp.remote.database.user.current_user.updatehandler.UpdateTask;
import com.codecamp.chatapp.utility.toast.ToastController;

public class UpdateCurrentUserDialog {
    private Dialog dialog;
    private CurrentUserPresenter currentPersonPresenter;
    private ToastController toastController;
    private Context context;
    public UpdateCurrentUserDialog(Context context){
        this.context = context;
        currentPersonPresenter = new CurrentUserPresenter();
        toastController = new ToastController();
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.update_current_user_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void update(String placeHolder,final UpdateTask updateTask){
        EditText editText = dialog.findViewById(R.id.TITLE);
        Button button = dialog.findViewById(R.id.BUTTON);
        editText.setHint(placeHolder);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(placeHolder.equals("name")){
                    if(!TextUtils.isEmpty(editText.getText().toString())){
                        currentPersonPresenter.updateName(editText.getText().toString(),updateTask);
                        editText.getText().clear();
                    }else{
                        toastController.toast(context,"empty field!!");
                    }

                }

                if(placeHolder.equals("bio")){
                    if(!TextUtils.isEmpty(editText.getText().toString())){
                        currentPersonPresenter.updateBio(editText.getText().toString(),updateTask);
                        editText.getText().clear();
                    }else{
                        toastController.toast(context,"empty field!!");
                    }
                }

                if(placeHolder.equals("about")){
                    if(!TextUtils.isEmpty(editText.getText().toString())){
                        currentPersonPresenter.updateAbout(editText.getText().toString(),updateTask);
                        editText.getText().clear();
                    }else{
                        toastController.toast(context,"empty field!!");
                    }
                }

                if(placeHolder.equals("phone")){
                    if(!TextUtils.isEmpty(editText.getText().toString())){
                        currentPersonPresenter.updatePhone(editText.getText().toString(),updateTask);
                        editText.getText().clear();
                    }else{
                        toastController.toast(context,"empty field!!");
                    }
                }
            }
        });

    }

    public void showDialog(){
        dialog.show();
    }

    public void dismissDialog(){
        dialog.cancel();
    }
}
