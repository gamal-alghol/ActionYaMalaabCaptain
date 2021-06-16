package com.malaab.ya.action.actionyamalaab.utils;

import android.app.Activity;
import android.graphics.Color;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.malaab.ya.action.actionyamalaab.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validator {

    private Activity mActivity;
    private List<View> xFields = new ArrayList<>();
    private List<HashMap<String, Object>> maps = new ArrayList<>();

    private boolean isAnimationEnabled = true, isErrorEnabled = true;


    public Validator(Activity activity) {
        mActivity = activity;
    }


    public Validator setAnimationEnabled(boolean isEnabled) {
        isAnimationEnabled = isEnabled;
        return this;
    }

    public Validator setErrorEnabled(boolean isEnabled) {
        isErrorEnabled = isEnabled;
        return this;
    }


    public Validator addField(View view) {
        xFields.add(view);

        HashMap<String, Object> map = new HashMap<>();
        map.put("defaultHintText", ((EditText) view).getHint().toString());
        map.put("defaultHintColor", ((EditText) view).getCurrentHintTextColor());
        map.put("defaultTextColor", ((EditText) view).getCurrentTextColor());

        maps.add(map);

        return this;
    }

    public boolean isValid() {
        for (View view : xFields) {
            if (isEmpty(view, xFields.indexOf(view))) {
                view.setFocusableInTouchMode(true);
                view.requestFocus();
                return false;
            }
        }
        return true;
    }


    private boolean isEmpty(View view, int index) {
        if (view instanceof EditText) {
            if (TextUtils.isEmpty(((EditText) view).getText().toString().trim())) {
                ((EditText) view).setText("");

                if (isErrorEnabled) {
                    ((EditText) view).setHint(mActivity.getString(R.string.error_empty_field));
                    ((EditText) view).setHintTextColor(Color.RED);
                }

                if (isAnimationEnabled) {
                    YoYo.with(Techniques.Shake).playOn(view);
                }
                return true;
            }

            if (((EditText) view).getInputType() == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
                if (!isEmailValid(((EditText) view).getText().toString().trim())) {
                    ((EditText) view).setTextColor(Color.RED);
                    ((EditText) view).setHintTextColor(Color.RED);
                    Toast.makeText(mActivity, mActivity.getString(R.string.error_invalid_email), Toast.LENGTH_LONG).show();

                    YoYo.with(Techniques.Shake).playOn(view);
                    return true;
                }
            }

            ((EditText) view).setHint((CharSequence) maps.get(index).get("defaultHintText"));
            ((EditText) view).setHintTextColor((Integer) maps.get(index).get("defaultHintColor"));
            ((EditText) view).setTextColor((Integer) maps.get(index).get("defaultTextColor"));
        }

        return false;
    }

    private boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public Validator remove(View view) {
        for (View v : xFields) {
            if (v.equals(view)) {
                xFields.remove(v);
                break;
            }
        }
        return this;
    }

    public void empty() {
        xFields.clear();
    }


    public void onDestroy() {
        xFields = null;
        maps = null;

        mActivity = null;
    }

}