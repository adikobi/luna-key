package com.example.mykeyboard;

import android.app.Service;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.google.android.gms.common.util.ArrayUtils;

import java.net.URLDecoder;

public class EDMTKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private KeyboardView kv;
    private Keyboard keyboard;
    private Keyboard numbers;
    private boolean isRepitted = false;
    private int previous;
    private int[] endLetters = new int[]{1499, 1502, 1504, 1508, 1510, 39, 40};
    private int startTime;
    private int endTime;
    private int diff;

    // press ctrl+O
    @Override
    public View onCreateInputView(){
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        numbers = new Keyboard(this, R.xml.numbers);
        kv.setKeyboard(keyboard);
        kv.setPreviewEnabled(false);
        kv.setOnKeyboardActionListener(this);
        startTime = (int) System.currentTimeMillis();
        return kv;
    }

    @Override
    public void onPress(int i) {
        if (i == previous && ArrayUtils.contains(endLetters, i)) {
            isRepitted = true;
        }else{
            previous = i;
        }
    }

    @Override
    public void onRelease(int i) {
        startTime = endTime;
        isRepitted = false;
    }


    @Override
    public void onKey(int i, int[] ints) {
        InputConnection ic = getCurrentInputConnection();
        playClick(i);

        endTime = (int) System.currentTimeMillis();
        diff = endTime - startTime;
        if (isRepitted){
            if (diff<500){
                previous = -1;
                char code;
                if (i == 39 ) {
                    code = (char) ( 34 );
                }else if (i == 40 ){
                    code = (char) ( i + 1 );
                }else{
                    code = (char) (i - 1);
                }
                ic.deleteSurroundingText(1, 0);
                ic.commitText(String.valueOf(code), 1);



            }else{
                char code = (char) ( i );
                ic.commitText(String.valueOf(code), 1);
            }

        }else {
            switch (i) {
                case Keyboard.KEYCODE_DELETE:
                    ic.deleteSurroundingText(1, 0);
                    break;

                case Keyboard.KEYCODE_DONE:
                    ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    break;


                case -66:
                    // go to use numbers
                    kv.setKeyboard(numbers);
                    kv.setOnKeyboardActionListener(this);
                    break;

                case -200:
//                    return from numbers to letters
                    kv.setKeyboard(keyboard);
                    kv.setOnKeyboardActionListener(this);
                    break;

                default:
                    char code = (char) i;
                    ic.commitText(String.valueOf(code), 1);
            }
        }
    }



        private void playClick(int i)
    {

    }


    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}