package com.codecamp.chatapp.remote.database.user.users_list;

import com.codecamp.chatapp.bindings.models.User;

import java.util.List;

public interface OnUsersList {
    void showUsersList(List<User> users);
}
