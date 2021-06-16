package com.malaab.ya.action.actionyamalaab.utils;

import android.view.View;
import android.widget.EditText;


public class MultiFocusWatcher {

    private OnFocusChange listener;


    public MultiFocusWatcher setCallback(OnFocusChange listener) {
        this.listener = listener;
        return this;
    }


    public MultiFocusWatcher registerEditText(final EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (listener != null) {
                    listener.onFocusChange(editText, hasFocus);
                }
            }
        });
        return this;
    }


    public MultiFocusWatcher removeFocusListener(final EditText editText) {
        editText.setOnFocusChangeListener(null);
        return this;
    }


    public interface OnFocusChange {
        void onFocusChange(EditText editText, boolean hasFocus);
    }
}