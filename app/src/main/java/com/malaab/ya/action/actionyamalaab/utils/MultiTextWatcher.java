package com.malaab.ya.action.actionyamalaab.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.annotations.EditTextAction;


public class MultiTextWatcher {

    private OnTextWatcher listener;


    public MultiTextWatcher setCallback(OnTextWatcher listener) {
        this.listener = listener;
        return this;
    }


    public MultiTextWatcher registerEditText(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (listener != null) {
                    listener.beforeTextChanged(editText, s, start, count, after);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (listener != null) {
                    listener.onTextChanged(editText, s, start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (listener != null) {
                    listener.afterTextChanged(editText, editable);
                }
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (listener != null) {
                        listener.onEditorAction(editText, EditTextAction.IME_ACTION_SEARCH, editText.getText());
                    }
                    return true;

                } else if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (listener != null) {
                        listener.onEditorAction(editText, EditTextAction.IME_ACTION_GO, editText.getText());
                    }
                    return true;

                } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (listener != null) {
                        listener.onEditorAction(editText, EditTextAction.IME_ACTION_DONE, editText.getText());
                    }
                    return true;
                }

                return false;
            }
        });

        return this;
    }


    public MultiTextWatcher removeChangedListener(final EditText editText) {
        editText.addTextChangedListener(null);
        return this;
    }

    public MultiTextWatcher removeActionListener(final EditText editText) {
        editText.setOnEditorActionListener(null);
        return this;
    }


    public interface OnTextWatcher {

        void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after);

        void onTextChanged(EditText editText, CharSequence s, int start, int before, int count);

        void afterTextChanged(EditText editText, Editable editable);

        void onEditorAction(EditText editText, @EditTextAction int action, Editable editable);
    }
}