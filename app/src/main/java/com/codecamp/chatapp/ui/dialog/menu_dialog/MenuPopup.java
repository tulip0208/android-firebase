package com.codecamp.chatapp.ui.dialog.menu_dialog;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.codecamp.chatapp.R;
import com.codecamp.chatapp.remote.auth.logout.LogoutHandler;
import com.codecamp.chatapp.remote.auth.logout.LogoutTask;
import com.codecamp.chatapp.utility.toast.ToastController;

import java.lang.reflect.Field;

public class MenuPopup {
    public void showMenuPopup(Context context, View view,final PopupMenuTask popupMenuTask){
        PopupMenu popupMenus = new PopupMenu(context,view);
        popupMenus.inflate(R.menu.current_user_menu);
        popupMenus.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.SIGNOUT){
                    popupMenuTask.onTask();
                }
                return false;
            }
        });
        popupMenus.show();
        try {
            Field popup = PopupMenu.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            Object menu = popup.get(popupMenus);
            menu.getClass().getDeclaredMethod("setForceShowIcon", Boolean.class).invoke(menu, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface PopupMenuTask{
        void onTask();
    }
}
