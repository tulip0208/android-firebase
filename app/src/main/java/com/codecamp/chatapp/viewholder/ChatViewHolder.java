package com.codecamp.chatapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codecamp.chatapp.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public TextView showMessage,isseenText,dateTextView,imgIsSeenText;
    public CircleImageView showUserImage;
    public ShapeableImageView imageMeassage;
    public RelativeLayout messageLayout,imageLayout;
    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
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
}
