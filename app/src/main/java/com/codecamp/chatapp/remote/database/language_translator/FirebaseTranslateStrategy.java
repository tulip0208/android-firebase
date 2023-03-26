package com.codecamp.chatapp.remote.database.language_translator;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class FirebaseTranslateStrategy implements TranslateStrategy {

    @Override
    public void translate(String text, String sourceLanguage, String targetLanguage, final TranslateListner translateListner) {
        if (text.equals("")) {
            translateListner.onTranslateFailed("please enter a text!");
        } else {
            translateListner.onTranslating("translating...");
            TranslatorOptions translatorOptions = new TranslatorOptions.Builder()
                    .setSourceLanguage(sourceLanguage)
                    .setTargetLanguage(targetLanguage)
                    .build();
            Translator translator = Translation.getClient(translatorOptions);
            translator.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    translator.translate(text).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String translatedText) {
                            translateListner.onTranslated(translatedText);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    translateListner.onTranslateFailed(e.getMessage());
                }
            });


        }
    }

}
