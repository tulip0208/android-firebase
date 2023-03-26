package com.codecamp.chatapp.remote.database.language_translator;

public interface TranslateListner {
    void onTranslating(String message);
    void onTranslated(String translatedText);
    void onTranslateFailed(String error);
}
