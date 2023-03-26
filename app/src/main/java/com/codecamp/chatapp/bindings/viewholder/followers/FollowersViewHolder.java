package com.codecamp.chatapp.bindings.viewholder.followers;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codecamp.chatapp.R;
import com.codecamp.chatapp.bindings.models.Followers;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowersViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView followersPhoto;
    public ConstraintLayout constraintLayout;
    public FollowersViewHolder(@NonNull View itemView) {
        super(itemView);
        followersPhoto = itemView.findViewById(R.id.userPhoto);
        constraintLayout = itemView.findViewById(R.id.constraintLayout);
    }

    public void bind(Followers followers,Context context){
        if(followers.getPhoto().equals("default")){
            followersPhoto.setImageResource(R.drawable.person_profile);
        }else{
            Glide.with(context).load(followers.getPhoto()).into(followersPhoto);
        }
    }
}
