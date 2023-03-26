package com.codecamp.chatapp.ui.dialog.language_dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.codecamp.chatapp.R;
import com.codecamp.chatapp.remote.database.language_translator.FirebaseTranslateStrategy;
import com.codecamp.chatapp.remote.database.language_translator.TranslateHelper;
import com.codecamp.chatapp.remote.database.language_translator.TranslateListner;
import com.codecamp.chatapp.remote.database.language_translator.TranslateStrategy;
import com.codecamp.chatapp.utility.constants.languages.Languages;

public class LanguageDialog{
    private Dialog dialog;
    private TextView englishLanguage,germanLanguage,frenchLanguage,spanishLanguage;
    private Languages languages;
    private TranslateHelper translateHelper;
    private TranslateStrategy translateStrategy;
    public LanguageDialog(Context context){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.language_layout);
        englishLanguage = dialog.findViewById(R.id.ENGLISH);
        germanLanguage = dialog.findViewById(R.id.GERMAN);
        frenchLanguage = dialog.findViewById(R.id.FRENCH);
        spanishLanguage = dialog.findViewById(R.id.SPANISH);
        languages = new Languages();
        translateStrategy = new FirebaseTranslateStrategy();
        translateHelper = new TranslateHelper(translateStrategy);
    }

    public void showDialog(String text){
        dialog.show();
        englishLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateHelper.translate(text, languages.ENGLISH, languages.ENGLISH, new TranslateListner() {
                    @Override
                    public void onTranslating(String message) {

                    }

                    @Override
                    public void onTranslated(String translatedText) {

                    }

                    @Override
                    public void onTranslateFailed(String error) {

                    }
                });
            }
        });

        germanLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        frenchLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        spanishLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void dismissDialog(){
        dialog.dismiss();
    }

    public interface OnLanguageSelectedListner{
        void english(String english);
        void german(String german);
        void french(String french);
        void spanish(String spanish);
    }
}
