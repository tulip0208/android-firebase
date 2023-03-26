package com.codecamp.chatapp.bindings.viewholder.users;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codecamp.chatapp.R;
import com.codecamp.chatapp.remote.error_handler.FirebaseErrorHandler;
import com.codecamp.chatapp.bindings.models.User;
import com.codecamp.chatapp.ui.dialog.progress_dialog.ProgressDialogHandler;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder{
    public TextView userName,onlineStatus;
    public CircleImageView userImage;
    public ConstraintLayout relativeLayout;
    public RelativeLayout onlineIndicator,offlineIndicator;
    public ConstraintLayout cardRelativeLayout,imageRelativeLayout;
    private Context context;
    private ProgressDialogHandler progressDialogHandler;
    private FirebaseErrorHandler firebaseErrorHandler;

    public UserViewHolder(@NonNull View itemView,Context context) {
        super(itemView);
        this.context = context;
        progressDialogHandler = new ProgressDialogHandler(context);
        firebaseErrorHandler = new FirebaseErrorHandler();
        userName = itemView.findViewById(R.id.user_nameID);
        userImage= itemView.findViewById(R.id.user_imageID);
        onlineStatus = itemView.findViewById(R.id.online_statusID);
        relativeLayout = itemView.findViewById(R.id.main_child_RelativeLayoutID);
        cardRelativeLayout = itemView.findViewById(R.id.cardRelativeLayoutID);
        onlineIndicator = itemView.findViewById(R.id.onlineIndicatorColorID);
        offlineIndicator = itemView.findViewById(R.id.offlineIndicatorColorID);
        imageRelativeLayout = itemView.findViewById(R.id.userImageRelativeLayoutID);
    }

    public void bindUsers(User user){
        userName.setText(user.getName());
        if(user.getStatus().equals("online"))
        {
            onlineIndicator.setVisibility(View.VISIBLE);
            offlineIndicator.setVisibility(View.INVISIBLE);
        }else{
            offlineIndicator.setVisibility(View.VISIBLE);
            onlineIndicator.setVisibility(View.INVISIBLE);
        }
        if(user.getBio().equals("default"))
        {
            onlineStatus.setText("Hi,i am using chat app");
        }else{
            onlineStatus.setText(user.getBio());
        }

        if(user.getImage().equals("default"))
        {
            userImage.setImageResource(R.drawable.avatar);
        }else {
            Glide.with(context).load(user.getImage()).into(userImage);
        }
    }

}
