package com.codecamp.chatapp.bindings.adapters.followers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codecamp.chatapp.R;
import com.codecamp.chatapp.bindings.models.Followers;
import com.codecamp.chatapp.utility.animations.AnimationController;
import com.codecamp.chatapp.bindings.viewholder.followers.FollowersViewHolder;

import java.util.List;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersViewHolder> {
    private Context context;
    private List<Followers> followersList;
    private AnimationController animationController;
    public FollowersAdapter(Context context, List<Followers> followersList) {
        this.context = context;
        this.followersList = followersList;
        animationController = new AnimationController();
    }

    @NonNull
    @Override
    public FollowersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.followers,parent,false);
        return new FollowersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersViewHolder holder, int position) {
        holder.bind(followersList.get(position),context);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(context,holder.constraintLayout);
            }
        });
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }
}
