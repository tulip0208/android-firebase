package com.codecamp.chatapp.bindings.adapters.chats;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.codecamp.chatapp.R;
import com.codecamp.chatapp.bindings.models.Chat;
import com.codecamp.chatapp.bindings.viewholder.chats.ChatViewHolder;
import com.codecamp.chatapp.remote.database.user.current_user.CurrentUserPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> chatList;
    private String imageUrl;
    private String currentUserId;
    private CurrentUserPresenter currentUserPresenter;

    public ChatAdapter(Context mContext, List<Chat> chatList, String imageUrl) {
        this.mContext = mContext;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
        currentUserPresenter = new CurrentUserPresenter();
        currentUserId = currentUserPresenter.getUserId();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT)
        {
            View rightView = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new ChatViewHolder(rightView,parent.getContext());
        }else {
            View leftView = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new ChatViewHolder(leftView,parent.getContext());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(chatList.get(position),imageUrl);
        if(chatList.get(position).getType().equals("message"))
        {
            if(position == chatList.size()-1 && chatList.get(position).getSender().equals(currentUserId))
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
            if(position == chatList.size()-1 && chatList.get(position).getSender().equals(currentUserId))
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

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatList.get(position).getSender().equals(currentUserId))
        {
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }

}
