package com.codecamp.chatapp.viewholder;

import android.view.View;
import android.widget.TextView;
import com.codecamp.chatapp.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowerViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView mFollowerImage;
    public TextView mFollowerName;
    public ConstraintLayout mConstraintLayout;
    public FollowerViewHolder(@NonNull View itemView) {
        super(itemView);
        mFollowerImage = itemView.findViewById(R.id.followerImage);
        mFollowerName = itemView.findViewById(R.id.followerName);
        mConstraintLayout = itemView.findViewById(R.id.constraintLayout);

    }
}
