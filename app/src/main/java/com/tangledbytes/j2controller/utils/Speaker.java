package com.tangledbytes.j2controller.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class Speaker implements TextToSpeech.OnInitListener {
    private static final String TAG = "Speaker";
    private final TextToSpeech tts;

    public Speaker(Context context) {
        tts = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.UK);
            tts.setSpeechRate(2.5f);
            say("TTS initiated");
            Log.d(TAG, "TTS successfully initiated");
        }
    }

    public void say(String toSpeak) {
        say(toSpeak, true);
    }

    public void say(String toSpeak, boolean flush) {
        int mode = flush ? TextToSpeech.QUEUE_FLUSH : TextToSpeech.QUEUE_ADD;
        tts.speak(toSpeak, mode, null);
    }
}
