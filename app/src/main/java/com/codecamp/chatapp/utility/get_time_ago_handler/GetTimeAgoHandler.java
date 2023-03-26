package com.codecamp.chatapp.utility.get_time_ago_handler;

import android.content.Context;

import com.codecamp.chatapp.bindings.models.GetTimesAgo;
import com.codecamp.chatapp.bindings.models.UserInfo;

public class GetTimeAgoHandler {
    private GetTimesAgo timesAgo;
    private Context context;

    public GetTimeAgoHandler(Context context){
        this.context = context;
        timesAgo = new GetTimesAgo();
    }

    public String provideTimesAgo(UserInfo userInfo){
        Long lastTime = Long.parseLong(userInfo.getTime_stamp());
        String lastSeenTime = timesAgo.getTimeAgo(lastTime,context);
        return lastSeenTime;
    }


}
