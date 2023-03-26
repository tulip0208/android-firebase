package com.codecamp.chatapp.remote.database.language_translator;

public interface TranslateStrategy {
    void translate(String text,String sourceLanguage,String targetLanguage,final TranslateListner translateListner);
}
