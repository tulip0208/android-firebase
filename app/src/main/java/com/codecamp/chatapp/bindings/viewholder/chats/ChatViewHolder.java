package com.codecamp.chatapp.bindings.viewholder.chats;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codecamp.chatapp.R;
import com.codecamp.chatapp.bindings.models.Chat;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public TextView showMessage,isseenText,dateTextView,imgIsSeenText;
    public CircleImageView showUserImage;
    public ShapeableImageView imageMeassage;
    public RelativeLayout messageLayout,imageLayout;
    private Context context;
    public ChatViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        showMessage = itemView.findViewById(R.id.show_messageID);
        showUserImage = itemView.findViewById(R.id.show_user_imageID);
        isseenText = itemView.findViewById(R.id.txt_seen);
        dateTextView = itemView.findViewById(R.id.dateTextViewID);
        imageMeassage = itemView.findViewById(R.id.image_messageID);
        imageMeassage.setShapeAppearanceModel(imageMeassage.getShapeAppearanceModel()
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED,15)
                .setTopLeftCorner(CornerFamily.ROUNDED,15)
                .setBottomLeftCorner(CornerFamily.ROUNDED,15)
                .setBottomRightCorner(CornerFamily.ROUNDED,15)
                .build());
        imgIsSeenText = itemView.findViewById(R.id.img_seen);
        messageLayout = itemView.findViewById(R.id.messageLayout);
        imageLayout = itemView.findViewById(R.id.imageLayout);
    }

    public void bind(Chat chat,String image){
        showMessage.setText(chat.getMessage());
        dateTextView.setText(chat.getDate());
        if(image.equals("default"))
        {
            showUserImage.setImageResource(R.drawable.avatar);
        }else {
            Glide.with(context).load(image).into(showUserImage);
        }
    }
}
