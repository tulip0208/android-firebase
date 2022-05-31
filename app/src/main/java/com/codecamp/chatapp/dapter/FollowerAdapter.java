package com.codecamp.chatapp.dapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codecamp.chatapp.CurrentUserActivity;
import com.codecamp.chatapp.MainActivity;
import com.codecamp.chatapp.Profileactivity;
import com.codecamp.chatapp.R;
import com.codecamp.chatapp.model.Follower;
import com.codecamp.chatapp.viewholder.FollowerViewHolder;

import java.util.ArrayList;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerViewHolder> {
    private Context mContext;
    private ArrayList<Follower> mFollowerList;

    public FollowerAdapter(Context mContext, ArrayList<Follower> mFollowerList) {
        this.mContext = mContext;
        this.mFollowerList = mFollowerList;
    }

    @NonNull
    @Override
    public FollowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.follower,parent,false);
        return new FollowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerViewHolder holder,int position) {
        if(mFollowerList.get(position).getFollowerImage().equals("default")){
            holder.mFollowerImage.setImageResource(R.drawable.avatar);
        }else{
            Glide.with(mContext).load(mFollowerList.get(position).getFollowerImage()).into(holder.mFollowerImage);
        }
        holder.mFollowerName.setText(mFollowerList.get(position).getFollowerName());
        holder.mConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_in_animation));
                Intent intent = new Intent(mContext, Profileactivity.class);
                intent.putExtra("userID",mFollowerList.get(position).getUserId());
                mContext.startActivity(intent);
                ((CurrentUserActivity)mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFollowerList.size();
    }
}
