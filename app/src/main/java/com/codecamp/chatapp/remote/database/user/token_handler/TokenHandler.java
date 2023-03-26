package com.codecamp.chatapp.remote.database.user.token_handler;

import com.codecamp.chatapp.notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class TokenHandler {
    private DatabaseReference mRef;
    private FirebaseUser currentUser;
    public TokenHandler(){
        mRef = FirebaseDatabase.getInstance().getReference().child("Tokens");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }
    public void updateToken(String token){
        Token token1 = new Token(token);
        mRef.child(currentUser.getUid())
                .setValue(token1);
    }
    public String getUserToken(){
        return FirebaseInstanceId.getInstance().getToken();
    }
}
