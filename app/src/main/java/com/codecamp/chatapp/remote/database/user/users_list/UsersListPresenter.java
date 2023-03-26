package com.codecamp.chatapp.remote.database.user.users_list;

import androidx.annotation.NonNull;

import com.codecamp.chatapp.bindings.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersListPresenter {
    private UserListRepository userListRepository;
    private OnUsersList onUsersList;
    public UsersListPresenter(OnUsersList onUsersList, UserListRepository userListRepository){
        this.userListRepository = userListRepository;
        this.onUsersList = onUsersList;
    }

    public void getUsersList(){
        userListRepository.getUsersList(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> users = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String password = dataSnapshot.child("password").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();
                    String bio = dataSnapshot.child("bio").getValue().toString();
                    String timestamp = dataSnapshot.child("time_stamp").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String about = dataSnapshot.child("about").getValue().toString();
                    users.add(new User(dataSnapshot.getKey(),name,image,email,password,status,timestamp,bio,phone,about));
                }
                onUsersList.showUsersList(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
