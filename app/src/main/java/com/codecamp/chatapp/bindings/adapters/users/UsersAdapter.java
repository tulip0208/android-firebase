package com.codecamp.chatapp.bindings.adapters.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codecamp.chatapp.R;
import com.codecamp.chatapp.bindings.models.User;
import com.codecamp.chatapp.utility.animations.AnimationController;
import com.codecamp.chatapp.bindings.viewholder.users.UserViewHolder;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder>{
    private Context context;
    private List<User> userList;
    private AnimationController animationController;
    private OnNavigateToProfilePage onNavigateToProfilePage;

    public UsersAdapter(Context context,List<User> userList,final OnNavigateToProfilePage onNavigateToProfilePage){
        this.context = context;
        this.userList = userList;
        animationController = new AnimationController();
        this.onNavigateToProfilePage = onNavigateToProfilePage;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_child_item,parent,false);
        return new UserViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bindUsers(userList.get(position));
        holder.cardRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(context,holder.relativeLayout);
                onNavigateToProfilePage.onNavigate(userList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}
