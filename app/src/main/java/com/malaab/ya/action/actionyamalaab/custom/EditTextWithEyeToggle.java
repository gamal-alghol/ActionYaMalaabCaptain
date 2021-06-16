package com.malaab.ya.action.actionyamalaab.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;


import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditTextWithEyeToggle extends RelativeLayout {

    @BindView(R.id.mEditText)
    EditText mEditText;
    @BindView(R.id.mButtonToggle)
    AppCompatImageButton mButtonToggle;

    private boolean isPinVisible;


    public EditTextWithEyeToggle(Context context) {
        this(context, null);
    }

    public EditTextWithEyeToggle(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            view = inflater.inflate(R.layout.custom_edittext_eye_toggle, this, true);

            ButterKnife.bind(this, view);

            init(context, attrs);
        }
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextWithEyeToggle, 0, 0);

        String hint = a.getString(R.styleable.EditTextWithEyeToggle_eHint);
        mEditText.setHint(hint);

        a.recycle();

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mEditText.hasFocus()) {
                    if (!StringUtils.isEmpty(editable.toString())) {
                        mButtonToggle.setVisibility(View.VISIBLE);
                    } else {
                        mButtonToggle.setVisibility(View.GONE);
                    }
                }
            }
        });
    }


    @OnClick(R.id.mButtonToggle)
    public void toggle() {
        mEditText.requestFocus();

        if (!StringUtils.isEmpty(mEditText.getText().toString())) {
            if (isPinVisible) {
                mEditText.setTransformationMethod(new PasswordTransformationMethod());
                mButtonToggle.setImageResource(R.drawable.icon_eye_open);
            } else {
                mEditText.setTransformationMethod(null);
                mButtonToggle.setImageResource(R.drawable.icon_eye_close);
            }

            isPinVisible = !isPinVisible;

            mEditText.setSelection(mEditText.getText().length());
        }
    }


    public void setHint(String hint) {
        mEditText.setHint(hint);
    }

    public void setText(String text) {
        mEditText.setText(text);
    }


    public EditText getEditText() {
        return mEditText;
    }

    public String getText() {
        return mEditText.getText().toString();
    }
}
