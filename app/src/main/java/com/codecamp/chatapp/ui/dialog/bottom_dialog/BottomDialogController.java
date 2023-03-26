package com.codecamp.chatapp.ui.dialog.bottom_dialog;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.codecamp.chatapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BottomDialogController {
    private ConstraintLayout skyBluTheme,greenTheme,orangeTheme;
    public BottomSheetDialog showBottomDialog(Context context){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        bottomSheetDialog.setContentView(R.layout.change_theme);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        skyBluTheme = bottomSheetDialog.findViewById(R.id.skyBlueTheme);
        greenTheme = bottomSheetDialog.findViewById(R.id.greenTheme);
        orangeTheme = bottomSheetDialog.findViewById(R.id.orangeTheme);
        bottomSheetDialog.show();
        return bottomSheetDialog;
    }

    public Theme provideThemes(){
        return new Theme(greenTheme,orangeTheme,skyBluTheme);
    }
}
