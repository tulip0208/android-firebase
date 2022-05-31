package com.codecamp.chatapp.dapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codecamp.chatapp.MainActivity;
import com.codecamp.chatapp.R;
import com.codecamp.chatapp.SignUpActivity;
import com.codecamp.chatapp.model.Chat;
import com.codecamp.chatapp.viewholder.ChatViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private TextView yesView,noView;
    private Dialog dialog;
    private Context mContext;
    private List<Chat> chatList;
    private String imageUrl;
    private DatabaseReference deleteMessageRef,singleItemReference;
    private String deleteMessageTimestamp;
    private Query removeMessageQuery;
    private String myUid;

    public ChatAdapter(Context mContext, List<Chat> chatList, String imageUrl) {
        this.mContext = mContext;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT)
        {
            View rview = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new ChatViewHolder(rview);
        }else {
            View lview = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new ChatViewHolder(lview);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        holder.showMessage.setText(chatList.get(position).getMessage());
        holder.dateTextView.setText(chatList.get(position).getDate());


        if(imageUrl.equals("default"))
        {
            holder.showUserImage.setImageResource(R.drawable.avatar);
        }else {
            Glide.with(mContext).load(imageUrl).into(holder.showUserImage);
        }



        if(chatList.get(position).getType().equals("message"))
        {
            if(position == chatList.size()-1 && chatList.get(position).getSender().equals(mCurrentUser.getUid()))
            {

                if(chatList.get(position).getIsseen().equals(true))
                {
                    holder.isseenText.setVisibility(View.VISIBLE);
                    holder.dateTextView.setVisibility(View.VISIBLE);
                    holder.imgIsSeenText.setVisibility(View.GONE);
                    holder.imageMeassage.setVisibility(View.GONE);
                    //holder.imageLayout.setVisibility(View.GONE);
                    holder.messageLayout.setVisibility(View.VISIBLE);
                    //holder.imageMeassage.setVisibility(View.INVISIBLE);
                    holder.showMessage.setVisibility(View.VISIBLE);
                    holder.dateTextView.setText(chatList.get(position).getDate());
                    holder.isseenText.setText("seen");

                }else{
                    holder.isseenText.setVisibility(View.VISIBLE);
                    holder.dateTextView.setVisibility(View.VISIBLE);
                    holder.showMessage.setVisibility(View.VISIBLE);
                    holder.imgIsSeenText.setVisibility(View.GONE);
                    holder.imgIsSeenText.setVisibility(View.GONE);
                    holder.messageLayout.setVisibility(View.VISIBLE);
                    //holder.imageLayout.setVisibility(View.GONE);
                    //holder.imageMeassage.setVisibility(View.INVISIBLE);
                    holder.isseenText.setText("delivered");
                }

            }else {

                holder.isseenText.setVisibility(View.GONE);
                holder.dateTextView.setVisibility(View.VISIBLE);
                holder.imageMeassage.setVisibility(View.VISIBLE);
                holder.showMessage.setVisibility(View.VISIBLE);
                holder.imgIsSeenText.setVisibility(View.VISIBLE);
                holder.imageLayout.setVisibility(View.GONE);
                holder.messageLayout.setVisibility(View.VISIBLE);
                //holder.imageMeassage.setVisibility(View.INVISIBLE);

            }
        }

        if(chatList.get(position).getType().equals("image"))
        {
            if(position == chatList.size()-1 && chatList.get(position).getSender().equals(mCurrentUser.getUid()))
            {
                Glide.with(mContext).load(chatList.get(position).getMessage()).into(holder.imageMeassage);
                holder.imageMeassage.setVisibility(View.VISIBLE);
                holder.imgIsSeenText.setVisibility(View.VISIBLE);
                holder.messageLayout.setVisibility(View.GONE);
                holder.imageLayout.setVisibility(View.VISIBLE);
                holder.isseenText.setVisibility(View.GONE);
                holder.showMessage.setVisibility(View.GONE);
                holder.dateTextView.setVisibility(View.VISIBLE);
            }

        }

        holder.imageMeassage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(chatList.get(position).getType().equals("image")){
                    holder.imageMeassage.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
                    Toast.makeText(mContext,"image clicked "+position+" photo",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        holder.showMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(chatList.get(position).getType().equals("message")){
                    holder.showMessage.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
                    Toast.makeText(mContext,"message clicked "+position+" message",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }

    private void deleteMessage(int position)
    {

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(mCurrentUser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }

}
