package com.codecamp.chatapp.remote.database.user.users_list;

import com.google.firebase.database.ValueEventListener;

public interface UserListRepository {
    void getUsersList(ValueEventListener listener);
}
