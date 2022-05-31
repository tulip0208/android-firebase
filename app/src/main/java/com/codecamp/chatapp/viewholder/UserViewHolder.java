package com.codecamp.chatapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.codecamp.chatapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public TextView userName,onlineStatus;
    public CircleImageView userImage;
    public ConstraintLayout relativeLayout;
    public RelativeLayout onlineIndicator,offlineIndicator;
    public ConstraintLayout cardRelativeLayout,imageRelativeLayout;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.user_nameID);
        userImage= itemView.findViewById(R.id.user_imageID);
        onlineStatus = itemView.findViewById(R.id.online_statusID);
        relativeLayout = itemView.findViewById(R.id.main_child_RelativeLayoutID);
        cardRelativeLayout = itemView.findViewById(R.id.cardRelativeLayoutID);
        onlineIndicator = itemView.findViewById(R.id.onlineIndicatorColorID);
        offlineIndicator = itemView.findViewById(R.id.offlineIndicatorColorID);
        imageRelativeLayout = itemView.findViewById(R.id.userImageRelativeLayoutID);
    }
}
