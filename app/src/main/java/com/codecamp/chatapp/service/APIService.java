package com.codecamp.chatapp.service;

import com.codecamp.chatapp.notifications.MyResponse;
import com.codecamp.chatapp.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAgiJV1wE:APA91bEJVjKThEFPw8zC2uC0sS4sEqPDtIcHVvrbIBSxrF2fsnWYPh7hB7VD_GfEEEx_Ts_KOCI7_OGU3JjUjtPgP_Qo8ZCNJrdlOMEsRshjMWjXP0QRSQPahfYQIr9Oo1fkvnVWxP88"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
