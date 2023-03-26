package com.codecamp.chatapp.remote.retrofit;

import com.codecamp.chatapp.notifications.Client;
import com.codecamp.chatapp.service.APIService;

public class ApiServiceHandler {
    private APIService apiService;
    public ApiServiceHandler(){
        apiService = Client
                .getClient("https://fcm.googleapis.com/")
                .create(APIService.class);
    }
    public APIService provideApiService(){
        return apiService;
    }
}
