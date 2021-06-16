package com.malaab.ya.action.actionyamalaab.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.malaab.ya.action.actionyamalaab.R;


public class PasswordStrength {

    //--------REQUIREMENTS--------
    private static int REQUIRED_LENGTH = 6;
    private static int MAXIMUM_LENGTH = 15;
    private static boolean REQUIRE_SPECIAL_CHARACTERS = true;
    private static boolean REQUIRE_DIGITS = true;
    private static boolean REQUIRE_LOWER_CASE = true;
    private static boolean REQUIRE_UPPER_CASE = false;

    private int resId;
    private int color;


    private PasswordStrength(int resId, int color) {
        this.resId = resId;
        this.color = color;
    }

    public CharSequence getText(android.content.Context ctx) {
        return ctx.getText(resId);
    }

    public int getColor() {
        return color;
    }


    public static PasswordStrength calculateStrength(Context context, String password) {
        int currentScore = 0;
        boolean sawUpper = false;
        boolean sawLower = false;
        boolean sawDigit = false;
        boolean sawSpecial = false;


        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (!sawSpecial && !Character.isLetterOrDigit(c)) {
                currentScore += 1;
                sawSpecial = true;
            } else {
                if (!sawDigit && Character.isDigit(c)) {
                    currentScore += 1;
                    sawDigit = true;
                } else {
                    if (!sawUpper || !sawLower) {
                        if (Character.isUpperCase(c))
                            sawUpper = true;
                        else
                            sawLower = true;

                        if (sawUpper && sawLower)
                            currentScore += 1;
                    }
                }
            }

        }

        if (password.length() > REQUIRED_LENGTH) {
            if ((REQUIRE_SPECIAL_CHARACTERS && !sawSpecial)
                    || (REQUIRE_UPPER_CASE && !sawUpper)
                    || (REQUIRE_LOWER_CASE && !sawLower)
                    || (REQUIRE_DIGITS && !sawDigit)) {
                currentScore = 1;
            } else {
                currentScore = 2;
                if (password.length() > MAXIMUM_LENGTH) {
                    currentScore = 3;
                }
            }
        } else {
            currentScore = 0;
        }

        switch (currentScore) {
            case 0:
                return new PasswordStrength(R.string.password_strength_weak, ContextCompat.getColor(context, R.color.red));
            case 1:
                return new PasswordStrength(R.string.password_strength_medium, ContextCompat.getColor(context, R.color.orange));
            case 2:
                return new PasswordStrength(R.string.password_strength_strong, ContextCompat.getColor(context, R.color.light_blue));
            case 3:
                return new PasswordStrength(R.string.password_strength_very_strong, ContextCompat.getColor(context, R.color.green_apple));
            default:
        }

        return new PasswordStrength(R.string.password_strength_very_strong, ContextCompat.getColor(context, R.color.green));
    }

}
