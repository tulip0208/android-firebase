package com.codecamp.chatapp.utility.animations;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.codecamp.chatapp.R;

public class AnimationController {
    public void fadeInAnimation(Context context, View view){
        view.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in));
    }

    public void slideInLeftAnim(Context context, View view){
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_left));
    }
}
