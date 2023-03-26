package com.codecamp.chatapp.remote.database.language_translator;

public class TranslateHelper {
    private TranslateStrategy translateStrategy;

    public TranslateHelper(TranslateStrategy translateStrategy) {
        this.translateStrategy = translateStrategy;
    }

    public void translate(String text, String sourceLanguage, String targetLanguage, final TranslateListner translateListner) {
        translateStrategy.translate(text, sourceLanguage, targetLanguage, translateListner);
    }
}
