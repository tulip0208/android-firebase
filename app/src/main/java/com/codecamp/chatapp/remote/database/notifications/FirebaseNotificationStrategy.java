package com.codecamp.chatapp.remote.database.notifications;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codecamp.chatapp.R;
import com.codecamp.chatapp.notifications.Data;
import com.codecamp.chatapp.notifications.MyResponse;
import com.codecamp.chatapp.notifications.Sender;
import com.codecamp.chatapp.notifications.Token;
import com.codecamp.chatapp.service.APIService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseNotificationStrategy implements NotificationStrategy{
    private APIService apiService;

    public FirebaseNotificationStrategy(APIService apiService){
        this.apiService = apiService;
    }

    @Override
    public void sendNotification(String sender,String receiver, String name, String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Token token = dataSnapshot.getValue(Token.class);
                    Data data = new Data(sender, R.mipmap.ic_launcher,name+":"+message,"New Message",receiver);
                    Sender sender = new Sender(data,token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200)
                                    {
                                        if(response.body().success != 1)
                                        {
                                            //Toast.makeText(getApplicationContext(),"Failed!!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
