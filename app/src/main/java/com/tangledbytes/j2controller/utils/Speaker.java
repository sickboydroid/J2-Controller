package com.tangledbytes.j2controller.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Locale;

public class Speaker implements TextToSpeech.OnInitListener {
    private final String TAG = "TTS";
    private TextToSpeech tts;
    private boolean initialized;
    public static int MODE_ADD = TextToSpeech.QUEUE_ADD;
    public static int MODE_FLUSH = TextToSpeech.QUEUE_FLUSH;

    public Speaker(Context context) {
        tts = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.UK);
            say("TTS initiated");
            initialized = true;
        }
    }

    public boolean hasInitialized() {
        return initialized;
    }

    public void say(String toSpeak) {
        say(toSpeak, MODE_ADD);
    }

    public void say(String toSpeak, int mode) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, TAG);
        tts.speak(toSpeak, mode, hashMap);
    }
}
