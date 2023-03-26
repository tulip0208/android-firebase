package com.codecamp.chatapp.utility.background;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

public class BackgroundController {
    public Background changeBackground(String color,int corner){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.parseColor(color));
        drawable.setCornerRadii(new float[] {corner, corner, corner, corner, corner, corner, corner, corner});
        //view.setBackground(drawable);
        return new Background(drawable,color);
    }
}
